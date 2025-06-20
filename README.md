
---
⚠️ Aclaración con fines funcionales del proyecto

Este proyecto ha sido desarrollado con fines educativos como parte del proyecto final de un curso de Formación Profesional.

> **Importante:**  
> Aunque se descargue todo el proyecto e intente ejecutar desde Android Studio, el emulador **no funcionará correctamente**.  
> Esto se debe a que se han excluido mediante `.gitignore` los siguientes archivos sensibles, los cuales deben mantenerse **privados y protegidos**:

- `libs.versions.toml`
- `build.gradle.kts` (módulo `app`)
- `proguard.pro`
- `google-services.json`
- `strings.xml`

Estos archivos **sí están incluidos dentro del archivo `.zip` compartido**, para que se pueda ejecutar el proyecto localmente con la configuración correcta.

---
## 👥 Créditos del desarrollo y responsabilidades

Este proyecto fue realizado por los siguientes alumnos, con las siguientes responsabilidades técnicas y de documentación:

- **Adriana**  
  Se encargó del desarrollo inicial de la aplicación: diseñó la interfaz en boceto, creó el proyecto en Android Studio, configuró Firebase e implementó el sistema de inicio de sesión.  Se ocupó de la maquetación de la memoria del proyecto. No logró incluir la guía de usuario debido a la tardanza de su entrega.

- **Rubén**  
  Desarrolló el boceto de la sección *Eventos* y programó su lógica. Aunque no logró conectarla a Firebase con `listOf()`, creó una simulación funcional para representar su funcionamiento. También desarrolló por código la interfaz de la sección *Equipo*.  
  Además, durante la fase de documentación, se encargó de recopilar los vídeos enviados por los compañeros mostrando el funcionamiento de la aplicación.

- **Marc**  
  Era el encargado de desarrollar la lógica de la sección *Equipo*, pero no llegó a completarla, por lo que esta sección quedó como una pantalla visual sin funcionalidad. También creó la pestaña *Perfil*, la cual actualmente no recoge los datos de los usuarios     logueados desde Firebase Auth ni Firestore. Por esta razón, aunque se inicie sesión con diferentes cuentas, se muestran siempre los datos personales de Marc. El botón **Cerrar sesión** sí funciona, permitiendo comprobar la persistencia de sesión.  
  Respecto a la documentación, tenía la responsabilidad de elaborar la guía de usuario, pero esta fue enviada a otro compañero, quien finalmente se encargó de corregirla y maquetarla. Pero añadida por separado.


> ⚠️ **Limitaciones actuales de la app:**  
> - La pestaña *Perfil* no muestra datos personalizados por usuario autenticado.  
> - La sección *Equipo* es solo visual y carece de funcionalidad.
>   
>   -----------------------------------------------
>
