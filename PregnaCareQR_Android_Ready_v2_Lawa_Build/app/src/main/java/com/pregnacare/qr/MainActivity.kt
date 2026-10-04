package com.pregnacare.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class MainActivity : AppCompatActivity() {

    private val pink = Color.rgb(232, 61, 134)
    private val pinkDeep = Color.rgb(181, 29, 98)
    private val blush = Color.rgb(255, 239, 246)
    private val blush2 = Color.rgb(255, 248, 251)
    private val ink = Color.rgb(48, 29, 39)
    private val muted = Color.rgb(137, 112, 124)
    private val line = Color.rgb(244, 220, 231)
    private val green = Color.rgb(58, 157, 108)

    private var nameValue = ""
    private var icValue = ""
    private var readerConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = blush2
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        showHome()
    }

    private fun baseScroll(): LinearLayout {
        val scroll = ScrollView(this)
        scroll.setBackgroundColor(blush2)
        scroll.clipToPadding = false
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(18), dp(22), dp(34))
        }
        scroll.addView(root)
        setContentView(scroll)
        return root
    }

    private fun header(root: LinearLayout, back: Boolean = false) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        if (back) {
            val b = TextView(this).apply {
                text = "‹"
                textSize = 34f
                setTextColor(ink)
                gravity = Gravity.CENTER
                setOnClickListener { showHome() }
            }
            row.addView(b, LinearLayout.LayoutParams(dp(42), dp(48)))
        }
        val icon = TextView(this).apply {
            text = "♥"
            textSize = 20f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = round(pink, 15)
        }
        row.addView(icon, LinearLayout.LayoutParams(dp(42), dp(42)).apply { rightMargin = dp(10) })
        val brand = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        brand.addView(text("PregnaCare", 15, ink, true))
        brand.addView(text("SECURE QR IDENTIFICATION", 7, muted, true), lp(top = 2))
        row.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        val chip = TextView(this).apply {
            text = "●  SECURE"
            textSize = 8f
            setTextColor(green)
            gravity = Gravity.CENTER
            background = round(Color.WHITE, 50, line)
            setPadding(dp(10), dp(7), dp(10), dp(7))
        }
        row.addView(chip)
        root.addView(row)
    }

    private fun showHome() {
        val root = baseScroll()
        header(root)

        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(10), dp(48), dp(10), dp(30))
        }
        val heart = TextView(this).apply {
            text = "♥"
            textSize = 38f
            setTextColor(pink)
            gravity = Gravity.CENTER
            background = round(Color.WHITE, 26, line)
            elevation = dp(6).toFloat()
        }
        hero.addView(heart, LinearLayout.LayoutParams(dp(88), dp(88)))
        val eyebrow = text("MOTHER & BABY SAFETY", 9, pink, true)
        eyebrow.gravity = Gravity.CENTER
        hero.addView(eyebrow, lp(top = 20))
        val title = text("PregnaCare  QR", 34, ink, true)
        title.gravity = Gravity.CENTER
        hero.addView(title, lp(top = 7))
        val sub = text("Secure patient identification\nmade simple.", 13, muted, false)
        sub.gravity = Gravity.CENTER
        hero.addView(sub, lp(top = 8))
        root.addView(hero)

        val card = card()
        val box = inner(card)
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val badge = TextView(this).apply {
            text = "▣"
            textSize = 21f
            setTextColor(pink)
            gravity = Gravity.CENTER
            background = round(blush, 15)
        }
        row.addView(badge, LinearLayout.LayoutParams(dp(46), dp(46)))
        val words = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        words.addView(text("MyKad Reader", 13, ink, true))
        words.addView(text("Connect your smart card reader to continue.", 10, muted, false), lp(top = 4))
        row.addView(words, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(12) })
        val ready = text("READY", 8, green, true)
        row.addView(ready)
        box.addView(row)
        root.addView(card, lp(top = 6))

        val read = primaryButton("READ MYKAD", "→")
        root.addView(read, lp(top = 16))
        read.setOnClickListener { showReader() }

        val note = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val lock = text("✦", 15, pink, true)
        note.addView(lock, LinearLayout.LayoutParams(dp(28), dp(28)))
        val nt = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        nt.addView(text("Privacy protected", 10, ink, true))
        nt.addView(text("Only Name + IC are used in the QR.", 9, muted, false), lp(top = 2))
        note.addView(nt, LinearLayout.LayoutParams(0, -2, 1f).apply { leftMargin = dp(6) })
        root.addView(note, lp(top = 18))
        footer(root)
    }

    private fun showReader() {
        val root = baseScroll()
        header(root, true)
        root.addView(text("STEP 01 / 03", 9, pink, true), lp(top = 28))
        root.addView(text("Connect MyKad", 29, ink, true), lp(top = 6))
        root.addView(text("Connect the SecureMetric-compatible reader,\nthen place the MyKad in the slot.", 12, muted, false), lp(top = 6))

        val card = card()
        val box = inner(card)
        val device = TextView(this).apply {
            text = "IDENTIV\n\n▰────────▰"
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
            background = round(Color.rgb(64, 65, 72), 20)
        }
        box.addView(device, LinearLayout.LayoutParams(-1, dp(150)))
        val model = text("uTrust 2700 R", 16, ink, true)
        model.gravity = Gravity.CENTER
        box.addView(model, lp(top = 14))
        val model2 = text("SMART CARD READER", 8, muted, true)
        model2.gravity = Gravity.CENTER
        box.addView(model2, lp(top = 3))
        val status = TextView(this).apply {
            text = if (readerConnected) "●  Reader connected" else "●  Waiting for reader"
            textSize = 9f
            setTextColor(if (readerConnected) green else muted)
            gravity = Gravity.CENTER
            background = round(if (readerConnected) Color.rgb(238, 250, 243) else blush, 50)
            setPadding(dp(12), dp(8), dp(12), dp(8))
        }
        box.addView(status, lp(top = 13))
        root.addView(card, lp(top = 22))

        val check = primaryButton(if (readerConnected) "READER CONNECTED" else "CHECK READER", "✓")
        root.addView(check, lp(top = 16))
        check.setOnClickListener {
            readerConnected = true
            showReader()
        }

        val demo = secondaryButton("TEST WITH SAMPLE DATA")
        root.addView(demo, lp(top = 10))
        demo.setOnClickListener {
            readerConnected = true
            nameValue = "Nur Syazlina"
            icValue = "000000-00-0000"
            showData()
        }

        val hint = card()
        val hb = inner(hint)
        hb.addView(text("DEMO MODE", 8, pink, true))
        hb.addView(text("The sample button only tests the app flow. Real MyKad reading still needs the official SecureMetric MYID SDK/API.", 10, muted, false), lp(top = 5))
        root.addView(hint, lp(top = 14))
        footer(root)
    }

    private fun showData() {
        val root = baseScroll()
        header(root, true)
        root.addView(text("STEP 02 / 03", 9, pink, true), lp(top = 28))
        root.addView(text("Patient Information", 29, ink, true), lp(top = 6))
        root.addView(text("Confirm the information received from the MyKad reader.", 12, muted, false), lp(top = 6))

        val success = TextView(this).apply {
            text = "✓  MyKad information received"
            textSize = 10f
            setTextColor(green)
            background = round(Color.rgb(238, 250, 243), 15)
            setPadding(dp(13), dp(11), dp(13), dp(11))
        }
        root.addView(success, lp(top = 16))

        val card = card()
        val box = inner(card)
        addInfo(box, "FULL NAME", nameValue)
        addInfo(box, "IDENTITY CARD NUMBER", icValue, 18)
        root.addView(card, lp(top = 12))

        val generate = primaryButton("GENERATE QR", "⌁")
        root.addView(generate, lp(top = 16))
        generate.setOnClickListener { showQr() }
        val edit = secondaryButton("EDIT INFORMATION")
        root.addView(edit, lp(top = 10))
        edit.setOnClickListener { showEdit() }
        footer(root)
    }

    private fun showEdit() {
        val root = baseScroll()
        header(root, true)
        root.addView(text("EDIT INFORMATION", 9, pink, true), lp(top = 28))
        root.addView(text("Patient details", 29, ink, true), lp(top = 6))
        val card = card()
        val box = inner(card)
        val name = editField("FULL NAME", nameValue)
        val ic = editField("IDENTITY CARD NUMBER", icValue)
        box.addView(name, lp(top = 4))
        box.addView(ic, lp(top = 16))
        root.addView(card, lp(top = 20))
        val save = primaryButton("SAVE & CONTINUE", "✓")
        root.addView(save, lp(top = 16))
        save.setOnClickListener {
            nameValue = name.text.toString().trim()
            icValue = ic.text.toString().trim()
            if (nameValue.isBlank() || icValue.isBlank()) {
                Toast.makeText(this, "Sila lengkapkan Nama dan No. MyKad.", Toast.LENGTH_SHORT).show()
            } else showData()
        }
        footer(root)
    }

    private fun showQr() {
        val root = baseScroll()
        header(root, true)
        root.addView(text("STEP 03 / 03", 9, pink, true), lp(top = 28))
        root.addView(text("Your QR is ready", 29, ink, true), lp(top = 6))
        root.addView(text("Use this QR for quick patient identification.", 12, muted, false), lp(top = 6))

        val qrCard = card()
        val box = inner(qrCard)
        val qr = ImageView(this).apply {
            setImageBitmap(makeQr("$nameValue|$icValue"))
            setBackgroundColor(Color.WHITE)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        box.gravity = Gravity.CENTER_HORIZONTAL
        box.addView(qr, LinearLayout.LayoutParams(dp(270), dp(270)))
        val caption = text("SECURE PATIENT ID", 8, pink, true)
        caption.gravity = Gravity.CENTER
        box.addView(caption, lp(top = 15))
        val name = text(nameValue, 15, ink, true)
        name.gravity = Gravity.CENTER
        box.addView(name, lp(top = 5))
        val ic = text(icValue, 11, muted, false)
        ic.gravity = Gravity.CENTER
        box.addView(ic, lp(top = 3))
        root.addView(qrCard, lp(top = 20))

        val privacy = card()
        val pb = inner(privacy)
        pb.addView(text("QR CONTENT", 8, pink, true))
        pb.addView(text("Name + IC only. No phone number is included.", 10, muted, false), lp(top = 4))
        root.addView(privacy, lp(top = 12))

        val newQr = primaryButton("CREATE NEW QR", "↻")
        root.addView(newQr, lp(top = 16))
        newQr.setOnClickListener {
            nameValue = ""
            icValue = ""
            readerConnected = false
            showHome()
        }
        footer(root)
    }

    private fun addInfo(parent: LinearLayout, label: String, value: String, top: Int = 0) {
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        box.addView(text(label, 8, muted, true))
        box.addView(text(value, 16, ink, true), lp(top = 5))
        parent.addView(box, lp(top = top))
    }

    private fun editField(label: String, value: String): EditText {
        return EditText(this).apply {
            hint = label
            setText(value)
            textSize = 13f
            setTextColor(ink)
            setHintTextColor(muted)
            singleLine = true
            setPadding(dp(14), dp(13), dp(14), dp(13))
            background = round(Color.WHITE, 13, line)
        }
    }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        background = round(Color.WHITE, 22, line)
        elevation = dp(4).toFloat()
    }

    private fun inner(card: LinearLayout): LinearLayout {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
        }
        card.addView(box)
        return box
    }

    private fun primaryButton(title: String, end: String): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = round(pink, 17)
            elevation = dp(4).toFloat()
            setPadding(dp(18), 0, dp(15), 0)
        }
        val t = text(title, 11, Color.WHITE, true)
        t.gravity = Gravity.CENTER
        row.addView(t, LinearLayout.LayoutParams(0, dp(54), 1f))
        val e = text(end, 20, Color.WHITE, true)
        e.gravity = Gravity.CENTER
        row.addView(e, LinearLayout.LayoutParams(dp(28), dp(54)))
        return row
    }

    private fun secondaryButton(title: String): TextView = TextView(this).apply {
        text = title
        textSize = 10f
        setTextColor(pinkDeep)
        gravity = Gravity.CENTER
        typeface = Typeface.DEFAULT_BOLD
        background = round(blush, 17, line)
        setPadding(dp(12), 0, dp(12), 0)
        minHeight = dp(50)
    }

    private fun footer(root: LinearLayout) {
        val f = text("PregnaCare QR  •  Secure Patient Identification", 8, Color.rgb(170, 148, 158), false)
        f.gravity = Gravity.CENTER
        root.addView(f, lp(top = 22))
    }

    private fun text(value: String, size: Int, color: Int, bold: Boolean): TextView = TextView(this).apply {
        text = value
        textSize = size.toFloat()
        setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
    }

    private fun round(fill: Int, radius: Int, stroke: Int? = null): android.graphics.drawable.GradientDrawable = android.graphics.drawable.GradientDrawable().apply {
        setColor(fill)
        cornerRadius = dp(radius).toFloat()
        stroke?.let { setStroke(dp(1), it) }
    }

    private fun lp(top: Int = 0): LinearLayout.LayoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { topMargin = dp(top) }

    private fun makeQr(value: String): Bitmap {
        val size = 720
        val matrix: BitMatrix = MultiFormatWriter().encode(value, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (x in 0 until size) for (y in 0 until size) {
            bmp.setPixel(x, y, if (matrix[x, y]) Color.rgb(48, 29, 39) else Color.WHITE)
        }
        return bmp
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
