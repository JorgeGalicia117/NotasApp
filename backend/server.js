const express = require('express');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { DatabaseSync } = require('node:sqlite');

const PUERTO = process.env.PORT || 3000;
const SECRETO = process.env.JWT_SECRET || 'cambiar_este_secreto_en_produccion';

const db = new DatabaseSync('notasapp.db');
db.exec('PRAGMA foreign_keys = ON');

db.exec(`
  CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
  );
  CREATE TABLE IF NOT EXISTS notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    titulo TEXT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_creacion TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
  );
`);

const app = express();
app.use(cors());
app.use(express.json());

// Middleware que valida el token en el header Authorization
function verificarToken(req, res, next) {
  const cabecera = req.headers['authorization'];
  if (!cabecera || !cabecera.startsWith('Bearer ')) {
    return res.status(401).json({ mensaje: 'Token no enviado' });
  }
  const token = cabecera.substring(7);
  try {
    const datos = jwt.verify(token, SECRETO);
    req.usuario = { id: datos.id, nombre: datos.nombre };
    next();
  } catch (error) {
    return res.status(401).json({ mensaje: 'Token inválido o vencido' });
  }
}

// Registro de usuario
app.post('/api/register', (req, res) => {
  const { nombre, email, password } = req.body;
  if (!nombre || !email || !password) {
    return res.status(400).json({ mensaje: 'Nombre, email y contraseña son obligatorios' });
  }
  const existente = db.prepare('SELECT id FROM usuarios WHERE email = ?').get(email);
  if (existente) {
    return res.status(409).json({ mensaje: 'Ese email ya está registrado' });
  }
  const hash = bcrypt.hashSync(password, 10);
  const info = db
    .prepare('INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)')
    .run(nombre, email, hash);
  res.status(201).json({ mensaje: 'Usuario creado', id: info.lastInsertRowid });
});

// Login: devuelve el token y los datos del usuario
app.post('/api/login', (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) {
    return res.status(400).json({ mensaje: 'Email y contraseña son obligatorios' });
  }
  const usuario = db.prepare('SELECT * FROM usuarios WHERE email = ?').get(email);
  if (!usuario || !bcrypt.compareSync(password, usuario.password)) {
    return res.status(401).json({ mensaje: 'Credenciales incorrectas' });
  }
  const token = jwt.sign({ id: usuario.id, nombre: usuario.nombre }, SECRETO, { expiresIn: '8h' });
  res.json({
    token: token,
    usuario: { id: usuario.id, nombre: usuario.nombre, email: usuario.email },
  });
});

// Crear una nota del usuario autenticado
app.post('/api/notas', verificarToken, (req, res) => {
  const { titulo, contenido } = req.body;
  if (!titulo || !contenido) {
    return res.status(400).json({ mensaje: 'Título y contenido son obligatorios' });
  }
  const info = db
    .prepare('INSERT INTO notas (usuario_id, titulo, contenido) VALUES (?, ?, ?)')
    .run(req.usuario.id, titulo, contenido);
  const nota = db.prepare('SELECT * FROM notas WHERE id = ?').get(info.lastInsertRowid);
  res.status(201).json(nota);
});

// Obtener todas las notas del usuario autenticado
app.get('/api/notas', verificarToken, (req, res) => {
  const notas = db
    .prepare('SELECT * FROM notas WHERE usuario_id = ? ORDER BY id DESC')
    .all(req.usuario.id);
  res.json(notas);
});

app.listen(PUERTO, '0.0.0.0', () => {
  console.log('Servidor listo en el puerto ' + PUERTO);
});
