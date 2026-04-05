const router = require('express').Router();
const { create, getAll } = require('../controllers/recordController');
const auth = require('../middleware/auth');

router.post('/', auth, create);
router.get('/', auth, getAll);

module.exports = router;