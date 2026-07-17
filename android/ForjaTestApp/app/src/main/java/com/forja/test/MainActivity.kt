package com.forja.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.forja.ForjaRuntime
import com.forja.ForjaSession
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var outputText: TextView
    private lateinit var runSyncBtn: Button
    private lateinit var runAsyncBtn: Button
    private lateinit var sessionBtn: Button
    private lateinit var fibBtn: Button

    private var session: ForjaSession? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputText = findViewById(R.id.outputText)
        runSyncBtn = findViewById(R.id.runSyncBtn)
        runAsyncBtn = findViewById(R.id.runAsyncBtn)
        sessionBtn = findViewById(R.id.sessionBtn)
        fibBtn = findViewById(R.id.fibBtn)

        Log.i("ForjaDemo", "Forja version: ${ForjaRuntime.version()}")

        runSyncBtn.setOnClickListener { ejecutarSync() }
        runAsyncBtn.setOnClickListener { ejecutarAsync() }
        sessionBtn.setOnClickListener { probarSession() }
        fibBtn.setOnClickListener { probarFibonacci() }
    }

    override fun onDestroy() {
        super.onDestroy()
        session?.destruir()
        scope.cancel()
    }

    private fun mostrarOutput(texto: String) {
        runOnUiThread {
            outputText.append(texto + "\n")
        }
    }

    private fun mostrarError(error: String) {
        runOnUiThread {
            outputText.append("❌ ERROR: $error\n")
        }
    }

    // ─── 1. Ejecución síncrona (one-shot) ────────────────────────

    private fun ejecutarSync() {
        mostrarOutput("▶ Ejecutando script síncrono...")
        try {
            val result = ForjaRuntime.ejecutar("""
                escribir("¡Hola desde Forja en Android!")
                escribir("Esto se ejecuta en la VM nativa")
                variable x = 42
                variable y = 58
                escribir("Suma: " + (x + y))
            """)

            mostrarOutput("✅ OK (${result.duracionMs} ms, ${result.ejecutadas} instr)")
            for (line in result.output) {
                mostrarOutput("  │ $line")
            }
        } catch (e: Exception) {
            mostrarError(e.message ?: "Error desconocido")
        }
    }

    // ─── 2. Ejecución asíncrona (no bloquea UI) ──────────────────

    private fun ejecutarAsync() {
        mostrarOutput("▶ Ejecutando script asíncrono...")

        ForjaRuntime.ejecutarAsync(
            source = """
                escribir("Iniciando cálculo pesado...")
                variable suma = 0
                para (variable i = 0; i < 100; i = i + 1) {
                    suma = suma + i
                }
                escribir("Suma 0..99 = " + suma)
                escribir("¡Hecho desde un hilo background!")
            """,
            onResult = { result ->
                mostrarOutput("✅ Async OK (${result.duracionMs} ms)")
                for (line in result.output) {
                    mostrarOutput("  │ $line")
                }
            },
            onError = { error ->
                mostrarError(error.toString())
            }
        )
    }

    // ─── 3. Sesión con estado persistente ────────────────────────

    private fun probarSession() {
        mostrarOutput("▶ Probando sesión con estado...")

        try {
            // Crear sesión
            val s = ForjaRuntime.crearSession()
            session = s

            // Primera ejecución: definir variable
            s.ejecutar("""
                variable mensajes = arreglo[]
                funcion agregar(msg) {
                    mensajes.empujar(msg)
                }
                agregar("Primer mensaje")
                escribir("Mensajes: " + mensajes.length())
            """)

            // Segunda ejecución: la variable y función persisten
            s.ejecutar("""
                agregar("Segundo mensaje")
                agregar("Tercer mensaje")
                escribir("Total: " + mensajes.length())
                para (variable i = 0; i < mensajes.length(); i = i + 1) {
                    escribir("  [" + i + "] " + mensajes[i])
                }
            """)

            mostrarOutput("✅ Session OK")

            // No destruimos aún, lo hacemos en onDestroy
        } catch (e: Exception) {
            mostrarError(e.message ?: "Error en sesión")
        }
    }

    // ─── 4. Fibonacci (prueba de rendimiento) ────────────────────

    private fun probarFibonacci() {
        mostrarOutput("▶ Calculando Fibonacci(30)...")

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    ForjaRuntime.ejecutar("""
                        funcion fib(n) {
                            si (n < 2) { retornar n }
                            retornar fib(n - 1) + fib(n - 2)
                        }
                        variable n = 30
                        variable r = fib(n)
                        escribir("fib(" + n + ") = " + r)
                    """)
                }

                mostrarOutput("✅ Fibonacci: ${result.ejecutadas} instr en ${"%.2f".format(result.duracionMs)} ms")
                mostrarOutput("  │ ${result.text}")
            } catch (e: Exception) {
                mostrarError(e.message ?: "Error en Fibonacci")
            }
        }
    }
}
