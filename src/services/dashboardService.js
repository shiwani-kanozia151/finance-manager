const prisma = require('../config/prisma');

exports.calculateSummary = async () => {
  const records = await prisma.record.findMany();

  let totalIncome = 0, totalExpense = 0;

  records.forEach(r => {
    if (r.type === 'income') totalIncome += r.amount;
    else totalExpense += r.amount;
  });

  return {
    totalIncome,
    totalExpense,
    netBalance: totalIncome - totalExpense
  };
};