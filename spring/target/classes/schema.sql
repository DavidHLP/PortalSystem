CREATE TABLE IF NOT EXISTS repeater (
    type VARCHAR(50) PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    method VARCHAR(10) NOT NULL,
    body TEXT,
    response TEXT,
    status VARCHAR(20)
);

INSERT INTO repeater (type, url, method) VALUES
('get', 'http://localhost:8080/api/demo/test/get', 'GET'),
('post', 'http://localhost:8080/api/demo/test/post', 'POST'),
('put', 'http://localhost:8080/api/demo/test/put', 'PUT'),
('delete', 'http://localhost:8080/api/demo/test/delete', 'DELETE'); 