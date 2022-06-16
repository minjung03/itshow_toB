const express = require('express');
const http = require('http');
const url = require('url');
const fs = require('fs');
const querystring = require('querystring');
const app = express();

app.use(express.json());
app.set('port', process.env.PORT || 3000);

http.createServer(app).listen(app.get('port'), () =>{
  console.log("익스프레스 서버 시작 : "+app.get('port'));
});

const mysql = require('mysql');
// const { password } = require('pg/lib/defaults');
const pool  = mysql.createPool({
  connectionLimit : 10,
  host            : 'localhost',
  user            : 'root',
  password        : 'mirim3203',
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

    let u_email = req.params.u_email; //u_email는 path로 가져옵니다
    let userName = req.body.u_name; //body에 넣어서 가져옵니다.

    connection.query(`UPDATE user SET u_name="${userName}" WHERE u_email="${u_email}"`, function (error, results, fields) {
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

    console.log(req.body);

    let u_email = req.params.u_email; //u_email는 path로 가져옵니다

    connection.query(`DELETE FROM user WHERE u_email="${u_email}"`, function (error, results, fields) {
      // When done with the connection, release it.
      if(results){
        res.send(results)
      }
      if (error) throw error;

      connection.release();
    });

  });
});

//유저 정보 모두 조회(게시글 번호, 평점, ) ---- 아이디로 조회
app.get('/users/:u_email', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    let u_email = req.params.u_email; //u_email는 path로 가져옵니다

    connection.query(`SELECT * FROM user WHERE u_email="${u_email}"`, function (error, results, fields) {
      if(results){ res.send(results[0]) }
      if (error) throw error;
      connection.release();
    });

  });
});

//유저 정보를 닉네임으로 조회
app.get('/users', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    const u_name = req.query.u_name?req.query.u_name:null;//유저의 게시물들 조회

    connection.query(`SELECT * FROM user WHERE u_name="${u_name}"`, function (error, results, fields) {
      if(results){ res.send(results[0]) }
      if (error) throw error;

      connection.release();
    });

  });
});


// 유저 정보 저장
app.post('/users/new', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    const u_email = req.body.u_email?req.body.u_email:null;
    const u_name = req.body.u_name?req.body.u_name:null;

    const post  = {u_email:u_email, u_name:u_name, u_star:0.0};

    connection.query(`INSERT INTO user SET ?`, post, function (error, results, fields) {
      if(results){ res.send(results) }
      if (error) throw error;

      connection.release();
    });

  });
});


// =================== flow =================== //

//어떤 사람의 팔로잉 목록
app.get('/users/:u_email/flowings', (req, res)=>{
  const u_email = req.params.u_email;
  const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM follow as f
  JOIN tob_db.user AS u ON f.flowing = u.u_email
    WHERE f.u_email="${u_email}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(select_query, function (error, results, fields) {
        if(results){ 
          const flowings = {
            flowingList:results.slice()
          };
          res.send(flowings);
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
  const select_query = `SELECT u.u_email, u.u_name, u.u_star, u.u_img FROM flow as f
  JOIN tob_db.user AS u ON f.u_email = u.u_email
    WHERE f.following="${following}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(select_query, function (error, results, fields) {
        if(results){ 
          const flowers = {
            flowerList:results.slice()
          };
          res.send(flowers);
         }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ res.send(e); }
  });
});

//팔로우하기(튜플 생성)
app.post('/follow/new', (req, res)=>{
  const { u_email, flowing } = req.body;
  const insert_query = `INSERT INTO follow VALUES("${u_email}", "${flowing}");`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
    try{
      connection.query(insert_query, function (error, results, fields) {
        if(results){ res.send(results); }
        if (error) throw error;

      });
    }catch(e){res.send(e);}
  });
});


//언팔로우하기(튜플 제거)
app.delete('/follow/delete', (req, res)=>{
  const { u_email, following } = req.body;
  const delete_query = `DELETE FROM follow WHERE u_email="${u_email}" AND following="${following}";`;
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    try{
      connection.query(delete_query, function (error, results, fields) {
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
          if(results[0]) res.send(true); //팔로우중
          else res.send(false); //팔로우중이아님
         }
        if (error) throw error;
        connection.release();
      });
    }catch(e){ res.send(e); }
  });
});