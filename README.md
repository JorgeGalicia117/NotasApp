# NotasApp

Aplicación Android de notas personales. Permite iniciar sesión, crear notas y consultar el listado de notas guardadas en un backend propio.

## Tecnologías

- Android: Kotlin, XML Layouts, MVVM, LiveData, Retrofit, OkHttp y RecyclerView con CardView
- Backend: Node.js, Express, SQLite, JWT y bcrypt

## Estructura

- `app/` contiene el proyecto Android
- `backend/` contiene el servidor Node.js

## Pantallas

- Login con ConstraintLayout
- Dashboard con LinearLayout
- Creación de notas con ConstraintLayout
- Listado de notas con LinearLayout, RecyclerView y CardView

## Cómo ejecutar el backend

Requiere Node.js 22.5 o superior.

```
cd backend
npm install
npm start
```

El servidor queda en el puerto 3000. Para crear un usuario de prueba:

```
Invoke-RestMethod -Method Post -Uri http://localhost:3000/api/register -ContentType "application/json" -Body '{"nombre":"Jorge","email":"jorge@test.com","password":"1234"}'
```

## Cómo ejecutar la app

1. Abrir la carpeta raíz en Android Studio.
2. Editar `BASE_URL` en `app/src/main/java/com/example/notasapp/network/RetrofitClient.kt` con la IP de la computadora que corre el backend.
3. El celular y la computadora deben estar en la misma red Wi-Fi.
4. Ejecutar la app con Run.

## Endpoints

| Método | Ruta | Descripción | Token |
|---|---|---|---|
| POST | /api/register | Crea un usuario | No |
| POST | /api/login | Devuelve el token y el usuario | No |
| POST | /api/notas | Crea una nota del usuario autenticado | Sí |
| GET | /api/notas | Lista las notas del usuario autenticado | Sí |

El token se envía en el header `Authorization: Bearer <token>`.