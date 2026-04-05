const prisma = require('../config/prisma');

exports.create = async (req, res) => {
  const { amount, type, category, note } = req.body;

  const record = await prisma.record.create({
    data: {
      amount,
      type,
      category,
      note,
      userId: req.user.id
    }
  });

  res.status(201).json(record);
};

exports.getAll = async (req, res) => {
  const records = await prisma.record.findMany();
  res.json(records);
};