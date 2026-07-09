# Forja Examples

Más de 250 ejemplos del lenguaje **Forja** (.fa), organizados por tema y dificultad.

## Organización

### Fundamentos (01–30)

| Archivo | Tema |
|---------|------|
| `01_hola.fa` | Hello World |
| `02_variables.fa` | Variables y mutabilidad |
| `03_tipos.fa` | Tipos básicos |
| `04_operaciones.fa` | Operadores aritméticos |
| `05_condicionales.fa` | if/else con `si`/`sino` |
| `06_bucles.fa` | Bucles `mientras`, `para`, `repetir` |
| `07_funciones.fa` | Definición de funciones |
| `10_clases.fa` | Clases y objetos |
| `11_mapas.fa` | Mapas/diccionarios |
| `13_errores.fa` | Manejo de errores con `Resultado` |

### Temas avanzados (31–200)

- **POO**: Clases, métodos, herencia (`31_clase_metodos.fa` – `36_referencias.fa`)
- **Algoritmos**: Ordenamiento, búsqueda, recursividad (`131`–`150`)
- **Estructuras de datos**: Listas, árboles, grafos, hash maps (`141`–`150`)
- **Patrones de diseño**: Singleton, Factory, Observer, Strategy (`171`–`180`)
- **Concurrencia**: Hilos, canales, select (`126`–`130`, `216`)
- **FFI**: Interop con C/Rust (`184`–`185`)
- **Ownership**: Préstamos y referencias (`181`–`183`)

### GUI (200–309)

- `201_gui_ventana.fa` — Ventana básica
- `205_gui_hola.fa` — Hola Mundo con GUI
- `206_gui_contador.fa` — Contador interactivo
- `301_layout_responsive.fa` — Layout responsive
- `302_botones_material.fa` — Botones Material Design 3
- `309_expressive.fa` — Glassmorphism, gradientes

### Contratos y exactitud (400–501)

- `400_exacto.fa` — Cálculo exacto con precisión arbitraria
- `500_contratos.fa` — Design by Contract (requiere/asegura/siempre)

### Juegos (151–160)

- `152_piedra_papel_tijera.fa`
- `153_ahorcado.fa`
- `154_tateti.fa`
- `155_memorice.fa`

## Ejecutar ejemplos

```bash
# Con ForjaFast VM (por defecto)
forja run examples/01_hola.fa

# Con VM original
forja run examples/01_hola.fa --vm vm

# Con JIT nativo
forja run examples/01_hola.fa --vm jit

# Compilar a nativo y ejecutar
forja run examples/01_hola.fa --asm

# Tests
forja test examples/124_atributo_test.fa
```

## Repositorios relacionados

- [forja-lang/forja](https://github.com/forja-lang/forja) — Núcleo del lenguaje
- [forja-lang/docs](https://github.com/forja-lang/docs) — Documentación
- [forja-lang/vscode](https://github.com/forja-lang/vscode) — Extensión de VS Code
