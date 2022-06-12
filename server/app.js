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
const { password } = require('pg/lib/defaults');
const pool  = mysql.createPool({
  connectionLimit : 10,
  host            : 'localhost',
  user            : 'root',
  password        : '1234',
  database        : 'tob_db'
});

//

//게시글 가져오기
app.get('/recruitment', (req, res)=>{
  const u_id = req.query.u_id?req.query.u_id:null;//유저의 게시물들 조회
  const r_no = req.query.r_no?req.query.r_no:null;//어떤 게시물 조회

  pool.getConnection(function(err, connection) {
    if (err) throw err; // not connected!
   
    // Use the connection
    connection.query(`SELECT * FROM recruitment where u_id = ${u_id} || r_no = ${r_no}`, function (error, results, fields) {
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
    const { u_id, r_title, r_content, r_minPrice, r_endDate, r_order, r_location, r_category, r_imgPath } = req.body;
    const r_startDate = new Date();

    const post  = {u_id: u_id, r_title:r_title, r_content:r_content, r_minPrice:r_minPrice, r_inprogress:0, r_startDate:r_startDate,
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