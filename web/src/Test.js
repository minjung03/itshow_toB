import { useNavigate } from 'react'
import { Grommet, Button, Box, Grid, Text, Footer, Anchor, Header, Nav } from 'grommet';
import HelpContent from './HelpContent';
import Placeholder from 'react-bootstrap/Placeholder';
import ListGroup from 'react-bootstrap/ListGroup';
import Welcome from './Welcome';
import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';

import { createBrowserHistory } from "history";
import {
  Route,
  Routes,
  HashRouter
} from "react-router-dom";

import './assets/scss/style.scss';

// pages for this product
import Components from "./views/components/components.jsx";
import CustomComponents from "./views/custom-components/custom-components.jsx";

var hist = createBrowserHistory();


function Test() {
  return (
    <div>
      <Grommet>
        <Header align="center" direction="row" flex={false} justify="between" gap="medium"
          fill="horizontal" pad="medium">
          <Box align="center" justify="center" direction="row" gap="small">
            <Box align="center" justify="center" direction="row" gap="xsmall">
              <Text weight="bold" color="text-strong"> ToB </Text>
              <Text color="text-strong">Together Buy</Text>
            </Box>
          </Box>
        </Header>


        <HashRouter history={hist}>
          <Routes>
            <Route path="/" element={<Components />} />
          </Routes>
        </HashRouter>
        <Welcome />
        <img src='https://im3.ezgif.com/tmp/ezgif-3-0d7950d5be.gif' width="200px" height="350px" />

        <Grid
          rows={['small', 'small', 'small']}
          columns={['1', '1']}
          gap="small"
          areas={[
            // { name: 'header', start: [0, 0], end: [1, 1] },
            { name: 'nav', start: [0, 1], end: [1, 2] },
            { name: 'main', start: [1, 1], end: [1, 2] },
          ]}
        >

          <Box gridArea="nav" background="brand" className='padding-20'>
            ToB의 좋은점
            <ListGroup>
              <ListGroup.Item>Cras justo odio</ListGroup.Item>
              <ListGroup.Item>Dapibus ac facilisis in</ListGroup.Item>
              <ListGroup.Item>Morbi leo risus</ListGroup.Item>
              <ListGroup.Item>Porta ac consectetur ac</ListGroup.Item>
              <ListGroup.Item>Vestibulum at eros</ListGroup.Item>
            </ListGroup>
            <Placeholder xs={12} bg="info" className="white-letter" >ㅇㅓㅉㅓㄱㅜ</Placeholder>

          </Box>
          <Box gridArea="nav" background="light-5" />
          <Box gridArea="main" background="light-2" />

        </Grid>



        <Box align="center" background="graph-2" pad="medium">
          <Button
            label="hello world"
            primary
            onClick={() => alert('hello, world')}
          />
        </Box>
        <HelpContent />


        <Footer align="center" direction="row-responsive" flex={false} justify="between" gap="medium"
          fill="horizontal" pad="medium" background={{ "color": "background-front" }}>
          <Text>
            © 2020 Hewlett Packard Enterprise Development LP
          </Text>
          <Box align="center" justify="start" direction="row" gap="small">
            <Button label="Terms" plain />
          </Box>
        </Footer>
      </Grommet>

    </div>
  );
}

export default Test;
