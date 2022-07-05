var mysql      = require('mysql');
var db = mysql.createConnection({
  host     : process.env.DB_HOST,
  user     : process.env.DB_USER,
  password : process.env.DB_PASS,
  database : process.env.DB_NAME
});

/*
var mysql      = require('mysql');
var db = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'mirim3203',
  database : 'tob_db'
});
*/

console.log("db 연결");
db.connect();//db 연결
console.log("db state : "+db.state);
module.exports = db;