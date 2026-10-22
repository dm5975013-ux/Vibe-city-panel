# Vibe City Panel — proyecto Android inicial

Esta es una base de aplicación Android nativa (Java) con:
- Actividad principal para solicitar permiso de superposición.
- Servicio de panel flotante sobre otras aplicaciones.
- Botones AUTO APUNTADO y TRABAJO AUTOMÁTICO (solo interruptores de demostración).
- Botón para ocultar el panel y volver a abrirlo desde una notificación persistente.

IMPORTANTE:
- Los interruptores NO controlan Vibe City ni implementan aimbot/automatización del juego.
- Android requiere que el usuario conceda manualmente el permiso "Mostrar sobre otras apps".
- No se incluye un APK compilado. Este ZIP contiene el código fuente del proyecto.
- Para compilar hace falta Android Studio en un ordenador con Android SDK/Gradle configurados.

## Compilar en Android Studio
1. Descarga e instala Android Studio desde el sitio oficial de Android Developers.
2. Descomprime este ZIP.
3. Android Studio > Open y elige la carpeta `VibeCityPanelAndroid`.
4. Espera a que Gradle sincronice el proyecto.
5. Si Android Studio solicita instalar el Android SDK indicado, acepta.
6. En el menú Build, selecciona Build Bundle(s) / APK(s) > Build APK(s).
7. Usa el enlace "locate" que muestra Android Studio para encontrar `app-debug.apk`.
8. Copia el APK a tu teléfono y ábrelo. Si Android lo solicita, permite instalar aplicaciones desde esa fuente.

## Probar
1. Abre Vibe City Panel.
2. Pulsa "Permitir panel flotante".
3. En Ajustes de Android, habilita "Mostrar sobre otras apps" para Vibe City Panel.
4. Vuelve a la app y pulsa "Iniciar panel".
5. Abre Vibe City. El panel puede aparecer encima si Android y el juego lo permiten.
6. Usa el botón × para ocultar el panel. La notificación persistente permite regresar a la actividad y abrirlo otra vez.

## Requisitos
- Android Studio reciente.
- JDK/Gradle gestionados por Android Studio.
- SDK Android 35 (Android Studio puede descargarlo).
