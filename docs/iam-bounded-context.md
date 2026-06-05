# Identity & Access Management (IAM) Bounded Context

Este documento describe en detalle todas las funcionalidades implementadas en el **Bounded Context de IAM (Identity & Access Management)** de la plataforma Atelier. Este módulo provee toda la infraestructura y lógica de negocio para la autenticación de usuarios, registro de cuentas, manejo de sesiones, tokens de seguridad, recuperación de contraseñas y control de acceso.

## 1. Registro e Inicio de Sesión (Sign Up / Sign In)

El agregado raíz de este contexto es el **Usuario (`User`)**, el cual representa una cuenta de autenticación válida dentro de la plataforma.

*   **Registro de Usuarios (`SignUpCommand`)**:
    *   Permite registrar una nueva cuenta de usuario en el sistema utilizando una dirección de correo electrónico (`email`) y una contraseña (`password`).
    *   Soporta alternativamente el registro federado mediante un identificador de Google (`googleId`).
    *   Por defecto, toda nueva cuenta de usuario se inicializa en estado `ACTIVE`.
*   **Inicio de Sesión (`SignInCommand`)**:
    *   Valida las credenciales del usuario (correo y contraseña).
    *   Genera y retorna un token de autenticación (JWT o similar) y los detalles del usuario autenticado (`AuthenticatedUserResource`) para iniciar sesión de forma segura.

---

## 2. Gestión de Cuentas de Usuario

Provee las funciones necesarias para que el usuario pueda administrar y actualizar las propiedades críticas de su identidad.

*   **Actualizar Correo Electrónico (`UpdateUserEmailCommand`)**:
    *   Permite al usuario cambiar su dirección de correo de acceso (`email`), previa validación de unicidad.
*   **Actualizar Contraseña (`UpdateUserPasswordCommand`)**:
    *   Permite al usuario cambiar su contraseña actual, solicitando su clave anterior por motivos de seguridad y encriptando la nueva credencial.
*   **Desactivar Cuenta (`deactivate()`)**:
    *   Cambia el estado del usuario a `INACTIVE`, impidiendo cualquier inicio de sesión posterior sin eliminar físicamente el registro (borrado lógico).

---

## 3. Recuperación de Contraseña (Password Recovery)

Ofrece un flujo de autoservicio seguro para usuarios que han olvidado sus credenciales, evitando la intervención manual de los administradores.

*   **Solicitud de Recuperación (`GeneratePasswordRecoveryTokenCommand`)**:
    *   Genera un token de recuperación único y de corta duración (`PasswordRecoveryToken`) asociado al correo electrónico del usuario.
    *   Registra la fecha de expiración del token para evitar usos maliciosos.
    *   Dispara el proceso de envío de un correo electrónico con el token/enlace de recuperación.
*   **Restablecer Contraseña con Token (`ResetPasswordCommand`)**:
    *   Valida la vigencia y correspondencia del token de recuperación.
    *   Actualiza la contraseña del usuario con el nuevo valor provisto y marca el token como utilizado o lo elimina para que no pueda reusarse.

---

## 4. Consultas (Queries)

El módulo provee endpoints y servicios de consulta para mapear la identidad de los usuarios y verificar sus roles:

*   **Obtener Usuario por ID (`GetUserByIdQuery`)**:
    *   Recupera el perfil de autenticación básico de un usuario (ID, email, status).
*   **Obtener Usuario por Email (`GetUserByEmailQuery`)**:
    *   Utilizado para verificaciones internas, validaciones de unicidad y flujos de soporte.
*   **Obtener Roles de Perfil por ID de Usuario (`GetProfileRolesByUserIdQuery`)**:
    *   Permite consultar la relación entre la identidad de autenticación (`User`) y sus perfiles de negocio en el Bounded Context de Core (ej. saber si el usuario posee un rol de mecánico, administrador, cliente, etc., cruzando la información con las entidades de Core).

---

## 5. Diseño REST Pragmático y Seguridad

El API REST para el Bounded Context de IAM implementa estándares de seguridad modernos para la protección de recursos:
*   Los endpoints de autenticación se encuentran expuestos bajo la ruta `POST /api/v1/authentication/sign-in` y `POST /api/v1/authentication/sign-up`.
*   Los flujos de recuperación operan en `POST /api/v1/authentication/forgot-password` y `POST /api/v1/authentication/reset-password`.
*   Las modificaciones a cuentas existentes se exponen como recursos del usuario utilizando rutas RESTful específicas con verbos `PUT` (Ej. `PUT /api/v1/users/{userId}/email` y `PUT /api/v1/users/{userId}/password`), limitando así el riesgo de mutaciones accidentales o escalamiento de privilegios.
