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
app.put('/users/:u_id/renameUser', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    console.log(req.body);

    let userId = req.params.u_id; //u_id는 path로 가져옵니다
    let userName = req.body.u_name; //body에 넣어서 가져옵니다.

    connection.query(`UPDATE user SET u_name="${userName}" WHERE u_id="${userId}"`, function (error, results, fields) {
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
app.delete('/users/:u_id/deleteUser', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    console.log(req.body);

    let userId = req.params.u_id; //u_id는 path로 가져옵니다

    connection.query(`DELETE FROM user WHERE u_id="${userId}"`, function (error, results, fields) {
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
app.get('/users/:u_id', (req, res)=>{
  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!

    let userId = req.params.u_id; //u_id는 path로 가져옵니다

    connection.query(`SELECT * FROM user WHERE u_id="${userId}"`, function (error, results, fields) {
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