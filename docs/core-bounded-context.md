# Core Bounded Context

Este documento describe en detalle todas las funcionalidades implementadas en el **Bounded Context de Core** de la plataforma Atelier. Este módulo se encarga del modelado básico de la organización, la administración de talleres (Workshops), sucursales (Branches), perfiles organizacionales (Owners, Employees, Customers) y la gestión de planes de suscripción (Subscription Plans) que habilitan el uso de la plataforma.

## 1. Gestión de Talleres (Workshops)

El Taller (Workshop) representa el negocio registrado en la plataforma. Es la entidad organizativa de nivel superior ligada a un propietario (Owner).

*   **Crear Taller (`CreateWorkshopCommand`)**:
    *   Permite registrar un nuevo taller en la plataforma asociándolo a un Owner (`ownerId`).
    *   Registra información legal como la razón social (`businessName`), nombre de marca comercial (`brandName`) y el número de identificación fiscal o RUC (`taxId`).
    *   Establece una configuración por defecto para los intervalos de kilometraje de mantenimiento preventivo (`mileageIntervalConfig`).
*   **Actualizar Taller (`UpdateWorkshopCommand`)**:
    *   Permite modificar la razón social, nombre de marca, identificación fiscal e intervalos de kilometraje del taller.
*   **Consultas (Queries)**:
    *   **Obtener Taller por ID (`GetWorkshopByIdQuery`)**: Recupera la información detallada del taller.
    *   **Listar Talleres por Owner (`GetAllWorkshopsByOwnerIdQuery`)**: Retorna todos los talleres creados por un propietario específico.

---

## 2. Gestión de Sucursales (Branches)

Las sucursales (Branches) representan las locaciones físicas en las que opera el taller. Las operaciones mecánicas (Work Orders) ocurren a nivel de sucursal.

*   **Crear Sucursal (`CreateBranchCommand`)**:
    *   Genera una nueva sucursal vinculada a un taller (`workshopId`).
    *   Requiere definir un código único (`code`) para identificar la sucursal, un nombre descriptivo, dirección (`Address`) y un teléfono de contacto.
*   **Actualizar Sucursal (`UpdateBranchCommand`)**:
    *   Permite modificar el código, nombre, dirección y teléfono de la sucursal.
*   **Consultas (Queries)**:
    *   **Obtener Sucursal por ID (`GetBranchByIdQuery`)**: Recupera detalles de una sucursal específica.
    *   **Listar Sucursales por Taller (`GetAllBranchesByWorkshopIdQuery`)**: Permite retornar todas las sucursales vinculadas a un taller, soportando el modelo multi-tenant.

---

## 3. Perfiles Organizacionales (Owners, Employees, Customers)

El Bounded Context Core define perfiles enriquecidos de negocio que se asocian a las cuentas de usuario de la plataforma creadas en el módulo de IAM.

### A. Propietarios (Owners)
*   **Crear Owner (`CreateOwnerCommand`)**: Registra la información de un propietario asociada a su cuenta de autenticación (`userId`), incluyendo nombre completo (`PersonName`), documento de identidad (`Document`) y número de teléfono.
*   **Actualizar Owner (`UpdateOwnerCommand`)**: Permite actualizar su información personal.
*   **Eliminar Owner (`DeleteOwnerCommand`)**: Realiza el borrado del perfil de negocio.

### B. Empleados / Personal del Taller (Employees)
*   **Crear Empleado (`CreateEmployeeCommand`)**: Genera el perfil de un miembro del personal (administradores, asesores o mecánicos) vinculado a su cuenta (`userId`), con su nombre, documento y teléfono.
*   **Actualizar Empleado (`UpdateEmployeeCommand`)**: Modifica la información del empleado.
*   **Eliminar Empleado (`DeleteEmployeeCommand`)**: Remueve el perfil de empleado del sistema.

### C. Clientes (Customers)
*   **Crear Cliente (`CreateCustomerCommand`)**: 
    *   Genera el perfil de un cliente asociado a un `userId`.
    *   Diferencia si el cliente es una empresa (`isCorporate = true`) o persona natural (`isCorporate = false`).
    *   Para clientes corporativos, se requiere y valida la razón social (`businessName`). Para personas naturales, se requiere el nombre (`name`).
*   **Actualizar Cliente (`UpdateCustomerCommand`)**: Modifica el perfil. Valida que un cliente corporativo no pueda alterar su tipo de documento y que siempre proporcione la razón social.
*   **Eliminar Cliente (`DeleteCustomerCommand`)**: Remueve el perfil del cliente del sistema.

---

## 4. Gestión de Planes de Suscripción (Subscription Plans)

Define las reglas de negocio y los límites de uso asociados a los distintos planes de pago ofrecidos por Atelier.

*   **Asignar Suscripción (`AssignSubscriptionCommand`)**: Vincula un plan de suscripción activo a un taller (`Workshop`).
*   **Cancelar Suscripción (`CancelSubscriptionCommand`)**: Desactiva la suscripción actual de un taller.
*   **Entidad de Dominio (`SubscriptionPlan`)**:
    *   Define el costo mensual del plan (`monthlyPrice`).
    *   Controla los límites técnicos del taller, tales como: número máximo de dispositivos OBD2 (`maxObd2Devices`), snapshots mensuales por vehículo (`maxMonthlySnapshotsPerVehicle`), número máximo de clientes registrados (`maxCustomers`), y cuentas de staff/personal permitidas (`maxStaffAccounts`).

---

## 5. Diseño REST Pragmático

El API REST para el Bounded Context de Core separa claramente la creación y edición de perfiles y estructuras físicas a través de endpoints RESTful limpios:
*   `POST /api/v1/owners`, `/api/v1/employees`, `/api/v1/customers` para el registro inicial de perfiles.
*   `PUT /api/v1/customers/user/{userId}` para actualizar un perfil mediante el identificador del usuario de autenticación.
*   `DELETE /api/v1/customers/user/{userId}` para realizar la eliminación lógica/física del perfil.
*   `GET /api/v1/branches/{branchId}` y `GET /api/v1/branches/workshop/{workshopId}` para consultas del árbol organizacional de manera directa y optimizada.
