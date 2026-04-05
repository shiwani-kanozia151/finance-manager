const prisma = require('../config/prisma');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

exports.register = async (req, res) => {
  const { name, email, password, role } = req.body;

  const hash = await bcrypt.hash(password, 10);

  const user = await prisma.user.create({
    data: { name, email, password: hash, role }
  });

  res.status(201).json(user);
};

exports.login = async (req, res) => {
  const { email, password } = req.body;

  const user = await prisma.user.findUnique({ where: { email } });

  if (!user) return res.status(404).json({ message: 'User not found' });

  const valid = await bcrypt.compare(password, user.password);
  if (!valid) return res.status(401).json({ message: 'Invalid password' });

  const token = jwt.sign(user, process.env.JWT_SECRET, { expiresIn: '1h' });

  res.json({ token });
};