# Game Controls para Android 🎮

Biblioteca ligera que añade controles táctiles personalizables a tus juegos o simuladores en Android.

[![](https://jitpack.io/v/Stirven0/Game-Controls.svg)](https://jitpack.io/#Stirven0/Game-Controls)

## Características

- **Joystick virtual** totalmente personalizable
- Hat (perilla) que sigue el dedo sin cortarse
- Eventos enriquecidos:
  - `onJoystickMoved`
  - `onJoystickDown / Up`
  - `onJoystickDoubleTap`
  - `onJoystickLongPress`
  - `onJoystickReturn`
- Colores, tamaños y transparencia ajustables
- Sin dependencias externas
- Compatible con Android API 21+

## Uso rápido

1. Añade el módulo `game-controls` a tu proyecto.
2. Coloca el Joystick en tu layout:

```xml
<com.aa.game_controls.joystick.Joystick
    android:id="@+id/joystick"
    android:layout_width="200dp"
    android:layout_height="200dp"/>
```

3. Implementa el listener en tu `Activity` o `Fragment`:

```kotlin
joystick.setJoystickListener(object : Joystick.JoystickListener {
    override fun onJoystickMoved(xPercent: Float, yPercent: Float, id: Int) {
        player.move(xPercent, yPercent)
    }

    override fun onJoystickDoubleTap(xPercent: Float, yPercent: Float, id: Int) {
        player.sprint()
    }
})
```

Personalización

```kotlin
joystick.setBaseColor(Color.parseColor("#33FFFFFF"))
joystick.setHatColor(Color.BLACK)
joystick.setBaseSizeRatio(0.45f)  // 0.1 - 0.8
joystick.setHatSizeRatio(0.20f)   // 0.05 - 0.4
```

Estructura del proyecto

```
game-controls/
├── src/main/java/com/aa/game_controls/
│   ├── base/            # Clases base (GameControlBase, ControlConfig)
│   ├── joystick/        # Joystick + JoystickConfig
│   └── interfaces/      # Listeners genéricos
├── build.gradle.kts
└── proguard-rules.pro
```

Contribuir

1. Fork del repo
2. Crea tu rama (`git checkout -b feature/mi-mejora`)
3. Commit (`git commit -m "Añade tal funcionalidad"`)
4. Push (`git push origin feature/mi-mejora`)
5. Abre un Pull Request

Licencia

Este proyecto se distribuye bajo la licencia MIT.

Consulta el archivo `LICENSE` para más detalles.

---

¿Te gustaría añadir otro control (D-pad, botones, slider)?

Abre un issue y lo discutimos ¡juntos!
