const { calculateSummary } = require('../services/dashboardService');

exports.summary = async (req, res) => {
  const summary = await calculateSummary();
  res.json(summary);
};