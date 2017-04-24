//  Created by zhaomingyu on 15/12/2.
var http = require('http');
var url = require('url');
var fs = require('fs');
var querystring = require('querystring');
var database = [];
var std = {
    duplicate: false,
    duplicateDescription: false,
    usernameDup: "",
    idDup: "",
    phoneDup: "",
    emailDup: "",
    invalidDescription: ""
};

var server = http.createServer();

//   read jQuery
var readjQuery = function(response) {
    fs.readFile('./jquery-2.1.4.js', function(err, data) {
        console.log('Loading jQuery...');
        if (err) return err;
        response.writeHead(200, {'Content-Type': 'application/javascript'});
        response.write(data);
        response.end();
        console.log('Done');
    });
};

//   read the html and css files of the register page
var registerPage = function(response) {
    fs.readFile('./signUpPage.html', function (err, data) {
        console.log('Loading signUpPage.html...');
        if (err) throw err;
        response.writeHead(200, {'Content-Type': 'text/html'});
        response.write(data);
        response.end();
        console.log('Done');
    });
};
var registerStyle = function(response) {
    fs.readFile('./signUpPage.css', function(err, data) {
        console.log('Loading signUpPage.css...');
        if (err) throw err;
        response.writeHead(200, {'Content-Type': 'text/css'});
        response.write(data);
        response.end();
        console.log('Done');
    });
};
var registerAction = function(response) {
    fs.readFile("./signUpPage.js", function(err, data) {
        console.log('Loading signUpPage.js...');
        if (err) throw err;
        response.writeHead(200, {'Content-Type': 'application/x-javascript'});

        //response.write('var flag = false;');
        response.write('var message = \"\";');
        //  if you try to sign up the username twice,
        //  you will get a warning and return the register page
        if (std.duplicate) {
            //response.write('flag = true;');
            if (std.usernameDup != "")
                response.write('message = \"' + std.usernameDup + '\\n\";');
            if (std.idDup != "")
                response.write('message += \"' + std.idDup + '\\n\";');
            if (std.phoneDup != "")
                response.write('message += \"' + std.phoneDup + '\\n\";');
            if (std.emailDup != "")
                response.write('message += \"' + std.emailDup + '\\n\";');
            response.write('alert(message + \" existed\\nPlease Sign Up Again.\");');
        }
        response.write(data);
        response.end();
        console.log('Done');
    });
};

//  if user has signed up, load the detail page.
//  html and css
var successPage = function(response, info) {
    console.log('Loading Success Page...');
    response.writeHead(200, {'Content-Type': 'text/html'});
    //  build the dynamic html
    response.write('<!DOCTYPE html><html><head>');
    response.write('<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>');
    response.write('<script src=\"jquery-2.1.4.js\" language=\"JavaScript\"></script>');
    response.write('<script src=\"detail.js\" language=\"JavaScript\"></script>');
    response.write('<link rel=\"stylesheet\" href=\"detail.css\" />');
    response.write('<title>Detail</title></html><body>');
    response.write('<div id=\"main\">');
    response.write('<h1>用户详情</h1>');
    response.write('<form action=\"/\" method=\"post\">');
    response.write('<p>用户名:<input id=\"username\" class=\"mes\" disabled=\"disabled\" title=\"username\" value=\"' + info.username + '\"/></p>');
    response.write('<p>学号: <input class=\"mes\" disabled=\"disabled\" title=\"id\" value=\"' + info.id + '\"/></p>');
    response.write('<p>电话: <input class=\"mes\" disabled=\"disabled\" title=\"phone\" value=\"' + info.phone + '\"/></p>');
    response.write('<p>邮箱: <input class=\"mes\" disabled=\"disabled\" title=\"email\" value=\"' + info.email + '\"/></p>');
    response.write('</form>');
    response.write('<p><input id=\"return\" type=\"button\" value=\"return\" /></p></div>');
    response.write('</body></html>');
    response.end();

    console.log('Done');
};
var successStyle = function(response) {
    fs.readFile('./detail.css', function (err, data) {
        console.log('Loading Success Css...');
        if (err) throw err;
        response.writeHead(200, {'Content-Type': 'text/css'});
        response.write(data);
        response.end();
        console.log('Done');
    });
};
var successAction = function(response) {
    fs.readFile("./detail.js", function(err, data) {
        console.log('Loading detail.js...');
        if (err) throw err;
        response.writeHead(200, {'Content-Type': 'application/javascript'});
        response.write(data);
        response.end();
        console.log('Done');
    });
};

var register = function(request, response) {
    var info = '';
    std.duplicate = false;
    request.addListener('data', function(chunk) {
        info += chunk;
    }).addListener('end', function() {
        info = querystring.parse(info);
        if (!isExist(info)) {
            std.duplicate = false;
            //  judge valid
            if (!isValid(info)) {
                return registerPage(response);
            }
            //  print POST message
            console.log('register-------\nUser Info:');
            console.log('username: ' + info.username);
            console.log('id: ' + info.id);
            console.log('phone: ' + info.phone);
            console.log('email: ' + info.email + '');
            database.push(info);
            console.log('-------success');
            return successPage(response, info);
        } else {
            std.duplicate = true;
            //  alert("Username Existed");  can not do this! window is not defined
            return registerPage(response);
        }
    })
};

var requestFunction = function(request, response) {
    var i = 0, info;
    var objectUrl = url.parse(request.url);
    if (objectUrl.query != null) {
        var nameValue = objectUrl.query.slice(9);
        var tmpInfo = {};
        tmpInfo.username = nameValue;
        console.log(nameValue);
        if (isExist(tmpInfo)) {
            for (i = 0; i < database.length; ++i) {
                if (database[i].username == nameValue) {
                    info = database[i];
                }
            }
            return successPage(response, info);
        } else {
            //  The username you choose is existed already.
            return registerPage(response);
        }
    }
    //  read the html, css and javascript files of the register page
    if (objectUrl.pathname == '/') {
        if(request.method == 'POST') {
            //  read data from POST
            return register(request, response);
        }
        return registerPage(response);
    }
    if (objectUrl.pathname == '/signUpPage.css') {
        //  load signUpPage.css
        return registerStyle(response);
    }
    if (objectUrl.pathname == '/signUpPage.js') {
        return registerAction(response);
    }

    if (objectUrl.pathname == '/detail.css') {
        //  load detail.css
        return successStyle(response);
    }
    if (objectUrl.pathname == '/detail.js') {
        // load detail.js
        return successAction(response);
    }
    if (objectUrl.pathname == '/jquery-2.1.4.js') {
        return readjQuery(response);
    }
};

server.on('request', requestFunction);
server.listen(8000);
console.log('Listen in http://localhost:8000');

function isExist(userInfo) {
    var flag = false;
    std.usernameDup = "";
    std.idDup = "";
    std.phoneDup = "";
    std.emailDup = "";
    for (var i = 0; i < database.length; ++i) {
        if (database[i].username == userInfo.username) {
            std.usernameDup = userInfo.username;
            //std.duplicateDescription = true;
            flag = true;
        }
        if (database[i].id == userInfo.id) {
            std.idDup = userInfo.id;
            //std.duplicateDescription = true;
            flag = true;
        }
        if (database[i].email == userInfo.email) {
            std.emailDup = userInfo.email;
            //std.duplicateDescription = true;
            flag = true;
        }
        if (database[i].phone == userInfo.phone) {
            //std.duplicateDescription = true;
            std.phoneDup = userInfo.phone;
            flag = true;
        }
    }
    return flag;
}

function isValid(info) {
    if (!/^[a-zA-Z][a-zA-Z0-9_]{5,17}$/.test(info.username)){
        return false;
    }
    if (!/^[^0][0-9]{7}$/.test(info.id)) {
        return false;
    }
    if (!/^[^0][0-9]{10}$/.test(info.phone)) {
        return false;
    }
    if (!(/^[a-zA-Z0-9_\-]+@(([a-zA-Z_0-9\-])+\.)+[a-zA-Z]{2,4}$/.test(info.email))) {
        return false;
    }
    return true;
}