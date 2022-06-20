import React from 'react';
import { HashLink as Link } from 'react-router-hash-link';
import { Container, Row, Col } from 'reactstrap';

const HeaderBanner = () => {
    return (
        <div className="static-slider-head">
            <Container>
                <Row className="justify-content-center">
                    <Col lg="8" md="6" className="align-self-center text-center">
                        <h1 className="title">Together Buy!</h1>
                        <h4 className="subtitle font-light">미림인들을 위한 공동구매 앱, ToB!</h4>
                        <Link to="/#detail" className="btn btn-md m-t-30 btn-info-gradiant font-14">더 알아보기</Link>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default HeaderBanner;
