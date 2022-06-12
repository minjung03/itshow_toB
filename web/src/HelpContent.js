import React from 'react';
import { Grommet, Button, PageHeader, Anchor } from 'grommet';

function HelpContent() {
  return (
    <div>
      <Grommet>
        <PageHeader
          align="center"
          subtitle="어케해야하나요"
          parent={<Anchor label="도움말" />}
          actions={<Button label="Edit" primary />}
        />
        
      </Grommet>
    </div>
  );
}

export default HelpContent;
