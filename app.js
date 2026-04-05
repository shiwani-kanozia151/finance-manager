const express = require('express');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(express.json());

app.use('/api/users', require('./src/routes/userRoutes'));
app.use('/api/records', require('./src/routes/recordRoutes'));
app.use('/api/dashboard', require('./src/routes/dashboardRoutes'));

app.use((err, req, res, next) => {
  res.status(500).json({ message: err.message });
});

module.exports = app;