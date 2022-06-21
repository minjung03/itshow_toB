const express = require('express');
const mysql = require('mysql');
const db = require('./db.js');
const app = express();

// configuration =========================
app.use(express.json());
app.set('port', process.env.PORT || 3003);

app.listen(app.get('port'), () => {
	console.log('Express server listening on port ' + app.get('port'));
});

app.get('/', (req, res) => {
	res.statusCode = 200;
	res.setHeader('Content-Type', 'text/plain; charset=utf-8');
	res.end('안녕 노드?dddddd333\n');
});

// 정적 파일 불러오기
app.use(express.static(__dirname + "/web"));

app.get("/test", (req, res) => {
  res.sendFile(__dirname + "/web/index.html");
});

// ========= 게시글 관련 ========= //

//  전체 게시글  가져오기
app.get('/recruitment-list', (req, res) => {
	db.query(`SELECT * FROM recruitment`, function (error, results) {
		if (error) throw error;
		if (results) {
			console.log("recruitment-list 결과 : " + results);
			res.send(results);
		}
	});
});

//  게시글 번호로 게시글 가져오기
app.get('/recruitment', (req, res) => {
	const r_no = req.query.r_no; //게시글 번호로 게시글 가져오기
	db.query(`SELECT * FROM recruitment where r_no = '${r_no}'`, function (error, results) {
		if (error) throw error;
		if (results) {
			console.log("recruitment 결과 : " + results[0]);
			res.send(results[0]);
		}
	});
});

// 카테고리별 게시글 목록 가져오기
app.get('/recruitment-category', (req, res) => {
	const category = req.query.category;
	db.query(`SELECT * FROM recruitment where r_category = '${category}'`, function (error, results) {
		if (error) throw error;
		if (results) {
			console.log("recruitment category 결과 : " + results);
			res.send(results);
		}
	});

});

//게시글 생성하기
app.post('/recruitment/new', (req, res) => {
	const insert_query = `INSERT recruitment SET ?`;
	const { u_email, r_title, r_content, r_minPrice, r_endDate, r_order, r_location, r_category, r_imgPath } = req.body;
	const r_startDate = new Date();
	const post = {
		u_email: u_email, r_title: r_title, r_content: r_content, r_minPrice: r_minPrice, r_inprogress: 0, r_startDate: r_startDate,
		r_endDate: r_endDate, r_order: r_order, r_location: r_location, r_category: r_category, r_imgPath: r_imgPath ? r_imgPath : ""
	};
	db.query(insert_query, post, function (error, results) {
		if (error) throw error;
		if (results) {
			res.send(results);
		}
	});
});


// ========= USER =========== //

//유저 이름 바꾸기
app.put('/users/:u_email/renameUser', (req, res) => {
	const u_email = req.params.u_email; //u_email는 path로 가져옵니다
	const u_name = req.body.u_name; //body에 넣어서 가져옵니다.

	db.query(`UPDATE user SET u_name="${u_name}" WHERE u_email="${u_email}"`, function (error, results) {
		if (error) throw error;
		if (results) {
			res.send(results)
		}
	});

});

//유저를 삭제 (test 필요)
app.delete('/users/:u_email/deleteUser', (req, res) => {
	const u_email = req.params.u_email; //u_email는 path로 가져옵니다
	db.query(`DELETE FROM user WHERE u_email="${u_email}"`, function (error, results) {
		console.log(results);
		if (results) {
			if (!results[0]) {
				res.end("안 지워짐");
			}
			else {
				res.end("지워짐");
			}
		}
	});
});

// 유저 정보 저장 (test 필요)
app.post('/users/new', (req, res) => {
	const { u_email, u_name, u_img } = req.query;
	const post = { u_email: u_email, u_name: u_name, u_star: 0.0, u_img: u_img };
	const insert_query = `INSERT INTO user SET ?`;
	const select_query = `select * from user where u_email = '${u_email}'`;
	db.query(select_query, function (error, results) {
		if (error) throw error;
		if (results) {
			if (!results[0]) {
				db.query(insert_query, post, function (error, results) {
					if (error) throw error;
					if (results) { res.end("저장됨") }
				});
			}
			else { res.send(results) }
		}
	});

});

//유저 정보 모두 조회(현재는 평점과 이름만 조회됨) ---- 이메일로 조회
app.get('/users/:u_email', (req, res) => {
	const u_email = req.params.u_email; //u_email는 path로 가져옵니다
	db.query(`SELECT * FROM user WHERE u_email="${u_email}"`, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results[0]) }
	});

});

//유저 정보를 닉네임으로 조회
app.get('/users', (req, res) => {
	const u_name = req.query.u_name ? req.query.u_name : null;
	db.query(`SELECT * FROM user WHERE u_name="${u_name}"`, function (error, results, fields) {
		if (error) throw error;
		if (results) { res.send(results[0]) }
	});
});

// =========== search =========== // test 필요

//검색어 저장
app.post('/search/new', (req, res) => {
	const { u_email, s_word } = req.body;
	const select_query = `SELECT * FROM search WHERE u_email="${u_email}" AND s_word="${s_word}"`;
	const insert_query = `INSERT search SET ?`;
	const update_query = `UPDATE search SET s_cnt=s_cnt+1, s_time=now() WHERE u_email="${u_email}" AND s_word="${s_word}"`;
	db.query(select_query, function (error, results) {
		if (error) throw error;
		if (results) {
			if (results[0]) { 		//이 사람이 이 단어를 검색한적이 있으면 cnt를 더해주고 시간을 갱신해줌
				db.query(update_query, function (error, results) {
					if (error) throw error;
					if (results) { res.end("success update") }
				});
			} else {							//이 사람이 이 단어를 검색한 적이 없으면 생성
				const post = {
					u_email: u_email, s_word: s_word, s_time: new Date() 	//검색어를 저장하는 시간
				};
				db.query(insert_query, post, function (error, results) {
					if (error) throw error;
					if (results) { res.end("success insert") }
				});
			}
		}
	});
});

//인기 검색어 -> search 테이블에서 많이 검색된순
app.get('/search/popular', (req, res) => {
	db.query(`SELECT s_word FROM search GROUP BY s_word ORDER BY SUM(s_cnt) DESC;`, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results) }
	});

});

//누구의 최근 검색어인지...??
//최근 검색어 -> search 테이블에서 가장 최근순
app.get('/users/:u_email/recent-search', (req, res) => {
	const u_email = req.params.u_email
	db.query(`SELECT s_word FROM search WHERE u_email="${u_email}" ORDER BY s_time DESC;`, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results) }
	});

});

// 검색 키워드로 게시글 검색
app.post('/recruitment-search', (req, res) => {
	const search_word = req.query.search_word;//어떤 게시물 조회
	db.query(`SELECT * FROM recruitment WHERE r_title like '%${search_word}%' or r_content like '%${search_word}%'`, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results) }
	});

});

// =================== follow =================== //

//어떤 사람의 팔로잉 목록
app.get('/users/:u_email/followings', (req, res) => {
	const u_email = req.params.u_email;
	const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM follow as f
  JOIN user AS u ON f.following = u.u_email
    WHERE f.u_email="${u_email}";`;

	db.query(select_query, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results); }
	});

});

//어떤 사람의 팔로워 목록(그 사람을 팔로잉 한 사람들...)
app.get('/users/:following/followers', (req, res) => {
	const following = req.params.following;
	const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM follow as f
  JOIN user AS u ON f.u_email = u.u_email
    WHERE f.following="${following}";`;
	db.query(select_query, function (error, results) {
		if (error) throw error;
		if (results) { res.send(results); }
	});

});

//팔로우하기(튜플 생성)
app.post('/follow/new', (req, res) => {
	const { u_email, following } = req.body;

	const insert_query = `INSERT INTO follow VALUES("${u_email}", "${following}");`;
	db.query(insert_query, function (error, results) {
		if (error && error.sqlState == "23000") { res.send(results); }
		else if (error) throw error;
		if (results) { res.end(results); }
	});

});


//언팔로우하기(튜플 제거)
app.delete('/follow/delete', (req, res) => {
	const { u_email, following } = req.query;
	const delete_query = `DELETE FROM follow WHERE u_email="${u_email}" AND following="${following}";`;
	db.query(delete_query, function (error, results) {
		if (error) throw error;
		console.log("언팔로우:" + u_email + ", " + following);
		if (results) { res.end(results); }
	});

});

//어떤 사람이 이 사람을 팔로우하고있는지의 여부를 true, false로 리턴함.
app.get('/follow/whether', (req, res) => {
	const u_email = req.query.u_email;
	const following = req.query.following;
	const select_query = `SELECT * FROM follow WHERE u_email="${u_email}" AND following="${following}";`;
	db.query(select_query, function (error, results) {
		if (error) throw error;
		if (results) {
			if (results[0]) res.send("true"); //팔로우중
			else res.send("false"); //팔로우중이아님
		}
	});

});


// ============= 찜 ============== //

// 내가 찜한 글인지 체크
app.get('/postlike-user', (req, res)=>{

  const r_no = req.query.r_no?req.query.r_no:null;
  const u_email = req.query.u_email?req.query.u_email:null;

    console.log(r_no+ " "+u_email);
      db.query(`select COUNT(*) as "" from post_like where r_no = ${r_no} and u_email = '${u_email}';`, function (error, results, fields) {
        if (error) throw error;
        if(results){ 
          console.log(results)
          res.send(results) }
      });
});

// 게시글의 찜 초기 세팅
app.get('/postlike', (req, res)=>{
  const r_no = req.query.r_no?req.query.r_no:null;
    console.log(r_no);

      db.query(`select COUNT(*) as "" from post_like where r_no = ${r_no}`, function (error, results, fields) {
        if (error) throw error;
        if(results){ 
          console.log(results)
          res.send(results) }
      });
});

// 찜 상태 갱신
app.post("/postlikestate", (req, res)=>{

  const {r_no, u_email} = req.body

      db.query(`SELECT * FROM post_like WHERE u_email="${u_email}" AND r_no=${r_no}`, function (error, results, fields) {
        if (error) throw error;
        if(results){ 
          if(results[0]){ 
            db.query(`delete from post_like where r_no=${r_no} and u_email='${u_email}'`, function (error, results, fields) {
              if (error) throw error;
              if(results){ res.send("success") }
            });

          }else{
            db.query(`INSERT into post_like values (${r_no}, '${u_email}')`, function (error, results, fields) {
              if (error) throw error;
              if(results){ res.send("success") }
            });
          }
        }
      });
});


// 유저의 찜 목록 가져오기
app.get('/post-list', (req, res)=>{

  const u_email = req.query.u_email?req.query.u_email:null;
  const togle = req.query.togle?req.query.togle:null;
	const select_query = `
	SELECT p1.r_no, p1.u_email, cnt, r.* 
		FROM (post_like as p1 JOIN 
			(SELECT count(r_no) as cnt, r_no FROM post_like GROUP BY r_no) as p2
			JOIN recruitment r
			ON p1.r_no = p2.r_no AND p1.r_no = r.r_no)
		WHERE p1.u_email="${u_email}"
		ORDER BY cnt DESC;`;

    if(togle == 0){ // 인기
        db.query(select_query, function (error, results) {
						if (error) throw error;
          if(results){
            console.log("togle "+togle);
            res.send(results)
          }
        });
      } else {// 최근
        db.query(`select * from recruitment, post_like where recruitment.r_no = post_like.r_no and post_like.u_email = '${u_email}' order by r_startDate desc`, function (error, results) {
					if (error) throw error;
          if(results){
            console.log("togle "+togle);
            res.send(results)
          }
        });
      }
});


// ========= 이미지 업로드 ========= //
const multer = require('multer');
const crypto = require('crypto');
const fs = require('fs');
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
	filename: function (req, file, cb) {
		return crypto.pseudoRandomBytes(16, function (err, raw) {
			if (err) {
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

			if (file) {
				originalName = file.originalname;
				filename = file.fileName;//file.fileName
				mimeType = file.mimetype;
				size = file.size;
				console.log("execute" + fileName);
			} else {
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

//이미지 가져오기
app.get('/uploads/:upload', function (req, res) {

	var file = req.params.upload;
	console.log(file);
	var img = fs.readFileSync(__dirname + "/uploads/" + file);

	res.writeHead(200, { 'Content-Type': 'image/png' });
	res.end(img, 'binary');
});

