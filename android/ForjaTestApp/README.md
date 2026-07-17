# ForjaTestApp — Ejemplo de Forja en Android

App Android minimalista que demuestra cómo usar el runtime `forja-android-rt` para ejecutar scripts Forja en Android.

## Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- Android SDK 26+ (Android 8.0 Oreo)
- NDK (side-by-side) r27+
- Rust toolchain con targets Android:
  ```bash
  rustup target add aarch64-linux-android x86_64-linux-android armv7-linux-androideabi i686-linux-android
  ```

## Build del AAR

### En Linux/macOS
```bash
cd forja/
bash scripts/build-aar.sh
```

### En Windows (PowerShell)
```powershell
cd forja
.\scripts\build-aar.ps1
```

Esto genera `dist/forja-android-rt-0.8.2.aar`.

## Probar con la app demo

### 1. Copiar el AAR al proyecto

```bash
cp forja/dist/forja-android-rt-0.8.2.aar examples/android/ForjaTestApp/app/libs/
```

O en Windows:
```powershell
copy forja\dist\forja-android-rt-0.8.2.aar examples\android\ForjaTestApp\app\libs\
```

### 2. Abrir en Android Studio
```bash
cd examples/android/ForjaTestApp/
./gradlew assembleDebug
```

O en Windows:
```powershell
cd examples\android\ForjaTestApp
.\gradlew assembleDebug
```

## Estructura

```
ForjaTestApp/
├── app/
│   ├── libs/
│   │   └── forja-android-rt-0.8.2.aar    # Runtime Forja
│   ├── src/main/
│   │   ├── java/com/forja/test/
│   │   │   └── MainActivity.kt           # App demo
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## API Demo

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // One-shot: ejecutar y obtener resultado
        val result = ForjaRuntime.ejecutar("""
            escribir("Hola desde Forja en Android!")
            variable x = 42
            escribir("x = " + x)
        """)
        Log.d("Forja", result.output.join("\n"))

        // Con sesión (mantiene estado)
        val session = ForjaRuntime.crearSession()
        session.ejecutar("variable contador = 0")
        session.ejecutar("contador = contador + 1")
        session.ejecutar("escribir(contador)")  // → 1
        session.destruir()

        // Async (no bloquea UI)
        ForjaRuntime.ejecutarAsync("escribir("Hola async!")",
            onResult = { result -> miTextView.text = result.text },
            onError = { error -> mostrarError(error) }
        )
    }
}
```
