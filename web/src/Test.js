import React from 'react';
import { Grommet, Button, Box, Grid, Text, Footer, Anchor, Header, Nav } from 'grommet';
import HelpContent from './HelpContent';

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
  <Nav align="center" flex={false} direction="row">
    <Button label="공지사항" reverse={false} />
    <Button label="문의" reverse={false} />
  </Nav>
</Header>
        <Grid
          rows={['large', 'small', 'small']}
          columns={['1', '1']}
          gap="small"
          areas={[
            { name: 'header', start: [0, 0], end: [1, 0] },
            { name: 'nav', start: [0, 1], end: [0, 1] },
            { name: 'main', start: [1, 1], end: [1, 1] },
          ]}
        >

          <Box gridArea="header" > {/*background="brand"*/}
            <div>
              <p>ToB는 어쩌고 저쩌고</p>
              <p>어쩌고 저쩌고</p>
              <p>어쩌고 저쩌고</p>
              <p>어쩌고 저쩌고</p>
              <p>어쩌고 저쩌고</p>
              <p>어쩌고 저쩌고</p>
            </div>
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
