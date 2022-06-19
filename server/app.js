const express = require('express');
const http = require('http');
const app = express();

app.use(express.json());
app.set('port', process.env.PORT || 3003);


http.createServer(app).listen(app.get('port'), () =>{
  console.log("익스프레스 서버 시작 : "+app.get('port'));
});

const mysql = require('mysql');
// const { password } = require('pg/lib/defaults');
const pool  = mysql.createPool({
  connectionLimit : 10,
  host            : 'localhost',
  user            : 'root',
  password        : '1234',
  database        : 'tob_db'
});


//전체 게시글  가져오기
app.get('/recruitment-list', (req, res)=>{

  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
   
    // Use the connection
    connection.query(`SELECT * FROM recruitment`, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        console.log(results);
        res.send(results)
      }
   
      // Handle error after the release.
      if (error) throw error;
      connection.release();
      // Don't use the connection here, it has been returned to the pool.
    });
  });
});

//게시글 가져오기
app.get('/recruitment', (req, res)=>{
  const r_no = req.query.r_no?req.query.r_no:null;//어떤 게시물 조회

  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
   
    // Use the connection
    connection.query(`SELECT * FROM recruitment where r_no = '${r_no}'`, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        console.log(results[0]);
        res.send(results[0]);
      }
   
      // Handle error after the release.
      if (error) throw error;
      connection.release();
      // Don't use the connection here, it has been returned to the pool.
    });
  });
});

// 카테고리별 게시글 목록 가져오기
app.post('/recruitment-category', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    const category = req.query.category?req.query.category:null;
    console.log(category);

    // Use the connection
    connection.query(`SELECT * FROM recruitment where r_category = '${category}'`, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        console.log(results);
        res.send(results)
      }
   
      // Handle error after the release.
      if (error) throw error;
      connection.release();
      // Don't use the connection here, it has been returned to the pool.
    });

  });
});

//게시글 생성하기
app.post('/recruitment/new', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    console.log(req.body);

    //https://any-ting.tistory.com/14 < ---- 참고
    //r_no는 자동 증가.(기본키)
    //r_inprogress는 처음엔 0(완료된 공구가 아님.)
    //r_startDate는 여기에서 현재 시간을 넣어줌.
    const { u_email, r_title, r_content, r_minPrice, r_endDate, r_order, r_location, r_category, r_imgPath } = req.body;
    const r_startDate = new Date();

    const post  = {u_email: u_email, r_title:r_title, r_content:r_content, r_minPrice:r_minPrice, r_inprogress:0, r_startDate:r_startDate,
      r_endDate:r_endDate, r_order:r_order, r_location:r_location, r_category:r_category, r_imgPath:r_imgPath?r_imgPath:""};

    connection.query('INSERT INTO recruitment SET ?', post, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        res.send(results)
      }
      if (error) throw error;

      connection.release();
    });

  });
});

//유저 이름 바꾸기
app.put('/users/:u_email/renameUser', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    console.log(req.body);

    const u_email = req.params.u_email; //u_email는 path로 가져옵니다
    const u_name = req.body.u_name; //body에 넣어서 가져옵니다.

    connection.query(`UPDATE user SET u_name="${u_name}" WHERE u_email="${u_email}"`, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        res.send(results)
      }
      if (error) throw error;

      connection.release();
    });

  });
});

//유저를 삭제
app.delete('/users/:u_email/deleteUser', (req, res)=>{
  
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    const u_email = req.params.u_email; //u_email는 path로 가져옵니다

    connection.query(`DELETE FROM user WHERE u_email="${u_email}"`, function (error, results, fields) {

      console.log(results);
      if(results){
        if(!results[0]) { 
          res.send("못 지워짐")
        }
        else {
          res.send("지워짐")
          connection.release();
        }
      }
    
      connection.release();
    });

  });
});

//유저 정보 모두 조회(게시글 번호, 평점, ) ---- 아이디로 조회
app.get('/users/:u_email', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    const u_email = req.params.u_email; //u_email는 path로 가져옵니다

    connection.query(`SELECT * FROM user WHERE u_email="${u_email}"`, function (error, results, fields) {
      if(results){ res.send(results[0]) }
      if (error) throw error;
      connection.release();
    });

  });
});

// 유저 정보 저장
app.post('/users/new', (req, res)=>{
  const u_email = req.query.u_email?req.query.u_email:null;
  const u_name = req.query.u_name?req.query.u_name:null;
  const u_img = req.query.u_img?req.query.u_img:null;

  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    connection.query(`select * from user where u_email = '${u_email}'`, function (error, results, fields) {
      if(results){
        if(!results[0]){ 
          const post  = {u_email:u_email, u_name:u_name, u_star:0.0, u_img:u_img};
  
          connection.query(`INSERT INTO user SET ?`, post, function (error, results, fields) {
            if(results){ res.send("저장됨") }
            if (error) throw error;
            connection.release();
          });
        }
        else {
          res.send(results)
          connection.release();
        }
       
      }
     
      if (error) throw error;
    });
    
  });
});


//유저 정보를 닉네임으로 조회
app.get('/users', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    const u_name = req.query.u_name?req.query.u_name:null;

    connection.query(`SELECT * FROM user WHERE u_name="${u_name}"`, function (error, results, fields) {
      if(results){ res.send(results[0]) }
      if (error) throw error;

      connection.release();
    });

  });
});

//검색어 저장
app.post('/search/new', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    const { u_email, s_word } = req.body;
    try{
      connection.query(`SELECT * FROM search WHERE u_email="${u_email}" AND s_word="${s_word}"`, function (error, results, fields) {
        if(results){ 
          // res.send(results);/
          if(results[0]){ //이 사람이 이 단어를 검색한적이 있으면 cnt를 더해주고 시간을 갱신해줌
            connection.query(`UPDATE search SET s_cnt=s_cnt+1, s_time=now() WHERE u_email="${u_email}" AND s_word="${s_word}"`, function (error, results, fields) {
              if(results){ res.send("success") }
              if (error) throw error;
              connection.release();
            });
          }else{//이 사람이 이 단어를 검색한 적이 없으면 생성
            const post = {
              u_email: u_email,
              s_word: s_word,
              s_time: new Date() //검색어를 저장하는 시간
            };
            connection.query(`INSERT search SET ?`, post, function (error, results, fields) {
              if(results){ res.send("success") }
              if (error) throw error;
              connection.release();
            });
          }
        }
        if (error) throw error;

      });
   }catch(e){console.log(e);}
  });
});

//인기 검색어 -> search 테이블에서 많이 검색된순
app.get('/search/popular', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(`SELECT s_word FROM search GROUP BY s_word ORDER BY SUM(s_cnt) DESC;`, function (error, results, fields) {
        if(results){ res.send(results) }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ console.log(e); }

  });
});

//누구의 최근 검색어인지...??
//최근 검색어 -> search 테이블에서 가장 최근순
app.get('/users/:u_email/recent-search', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    const u_email = req.params.u_email

    try{
      connection.query(`SELECT s_word FROM search WHERE u_email="${u_email}" ORDER BY s_time DESC;`, function (error, results, fields) {
        if(results){ res.send(results) }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ console.log(e); }

  });
});

// 검색 키워드로 게시글 검색
app.post('/recruitment-search', (req, res)=>{

  const search_word = req.query.search_word?req.query.search_word:null;//어떤 게시물 조회

  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(`SELECT * FROM recruitment WHERE r_title like '%${search_word}%' or r_content like '%${search_word}%'`, function (error, results, fields) {
        // When done with the connection, release it.
      if(results){
        console.log(search_word);
        console.log(search_word);
        res.send(results)
      }
   
      // Handle error after the release.
      if (error) throw error;
      connection.release();
      // Don't use the connection here, it has been returned to the pool.
    });
    
   }catch(e){console.log(e);}

  });
});

// =================== flow =================== //

//어떤 사람의 팔로잉 목록
app.get('/users/:u_email/followings', (req, res)=>{
  const u_email = req.params.u_email;
  const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM follow as f
  JOIN user AS u ON f.following = u.u_email
    WHERE f.u_email="${u_email}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(select_query, function (error, results, fields) {
        if(results){
          console.log(results);
          res.send(results);
         }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ res.send(e); }

  });
});

//어떤 사람의 팔로워 목록(그 사람을 팔로잉 한 사람들...)
app.get('/users/:following/followers', (req, res)=>{
  const following = req.params.following;
  const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM follow as f
  JOIN user AS u ON f.u_email = u.u_email
    WHERE f.following="${following}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(select_query, function (error, results, fields) {
        if(results){
          console.log(results);
          res.send(results);
         }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ res.send(e); }
  });
});

//팔로우하기(튜플 생성)
app.post('/follow/new', (req, res)=>{
  const { u_email, following } = req.body;
  const insert_query = `INSERT INTO follow VALUES("${u_email}", "${following}");`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    try{
      connection.query(insert_query, function (error, results, fields) {
        if(results){ res.send(results); }
        if (error && error.sqlState == "23000") { res.send(results); }
        else if (error) throw error;

      });
    }catch(e){res.send(e);}
  });
});


//언팔로우하기(튜플 제거)
app.delete('/follow/delete', (req, res)=>{
  const { u_email, following } = req.query;
  const delete_query = `DELETE FROM follow WHERE u_email="${u_email}" AND following="${following}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(delete_query, function (error, results, fields) {
        console.log("언팔로우:"+u_email+", "+following);
        if(results){ res.send(results); }
        if (error) throw error;

      });
   }catch(e){res.send(e);}

  });
});

//어떤 사람이 이 사람을 팔로우하고있는지의 여부를 true, false로 리턴함.
app.get('/follow/whether', (req, res)=>{
  const u_email = req.query.u_email;
  const following = req.query.following;
  const select_query = `SELECT * FROM follow WHERE u_email="${u_email}" AND following="${following}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(select_query, function (error, results, fields) {
        if(results){ 
          if(results[0]) res.send("true"); //팔로우중
          else res.send("false"); //팔로우중이아님
         }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ res.send(e); }
  });
});

const multer = require('multer');

//파일 업로드
var _storage = multer.diskStorage({

	/*
	destination: function (req, file, callback) {

		//case: file type is image
		if(file.mimetype == "image/jpg" || file.mimetype == "image/jpeg" || file.mimetype == "image/png") {
			console.log("image");
			callback(null, "uploads/");
		} else {
			console.log("not image");
		}
	},
	filename: function (req, file, callback) {
		callback(null, register_number + "_" + file.originalname);
	}
	*/

	destination: 'uploads/',
	filename: function(req, file, cb) {
		return crypto.pseudoRandomBytes(16, function(err, raw) {
			if(err) {
				return cb(err);
			}
			//return cb(null, ""+(raw.toString('hex')) + (path.extname(file.originalname)));
			return cb(null, file.originalname);
		});
	}
});

//업로드
app.post('/upload', 
	multer({
		storage: _storage
	}).single('upload'), function (req, res) {

	try {

		let file = req.file;
		//const files = req.files;
		let originalName = '';
		let fileName = '';
		let mimeType = '';
		let size = 0;

		if(file) {
			originalName = file.originalname;
			filename = file.fileName;//file.fileName
			mimeType = file.mimetype;
			size = file.size;
			console.log("execute"+fileName);
		} else{ 
			console.log("request is null");
		}

	} catch (err) {

		console.dir(err.stack);
	}

	console.log(req.file);
	console.log(req.body);
	res.redirect("/uploads/" + req.file.originalname);//fileName

	return res.status(200).end();

});