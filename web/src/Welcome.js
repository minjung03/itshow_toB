import React, { useState } from 'react';

import Toast from 'react-bootstrap/Toast';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';

import './App.css';

const ExampleToast = ({ children }) => {
  const [show, toggleShow] = useState(true);

  return (
    <>
      {!show && <Button onClick={() => toggleShow(true)}>Show Toast</Button>}
      <Toast show={show} onClose={() => toggleShow(false)}>
        <Toast.Header>
          <strong className="mr-auto">환영합니다!</strong>
        </Toast.Header>
        <Toast.Body>{children}</Toast.Body>
      </Toast>
    </>
  );
};

const Welcome = () => (
  <Container className="p-3 float-left">
      <h1 className="header">"미림인들, 같이 사자!"</h1><br/>
      <ExampleToast>
      최소 주문 금액, 배송비 걱정은 그만!<br/>
        원하는 물품의 공구를 열어 여러 사람들과 함께 구매하세요!🎉
      </ExampleToast>
  </Container>
);

export default Welcome;
