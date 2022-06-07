const upload = require('../modules/multer');

router.post('/profile', AuthMiddleware.checkToken, 
 upload.single('profile'), UserController.updateProfile);//user/profile
