var mysql      = require('mysql');
var db = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '1234',
  database : 'tob_db'
});

console.log("db 연결");
db.connect();//db 연결
console.log("db state : "+db.state);
module.exports = db;
