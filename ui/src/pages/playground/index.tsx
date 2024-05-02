import React, { useState } from 'react';
import { Grid, Card, CardContent, Button } from '@mui/material';

const MyGridComponent = () => {
  const [activeCard, setActiveCard] = useState<number | null>(null);

  const data = [
    { id: 1, content: 'Content for Card 1' },
    { id: 2, content: 'Content for Card 2' },
    { id: 3, content: 'Content for Card 3' },
    // Add more cards as needed
  ];

  const toggleContent = (id: number) => {
    // Toggle the active card; if the same card is clicked again, deactivate it.
    setActiveCard(activeCard === id ? null : id);
  };

  return (
    <Grid container spacing={2}>
      {data.map((item) => (
        <Grid item xs={12} sm={6} md={4} key={item.id}>
          <Card>
            <CardContent>
              {activeCard === item.id ? (
                <SpecialComponent content={item.content} />
              ) : (
                <SpecialComponent content={item.content} />
              )}
              <Button onClick={() => toggleContent(item.id)}>
                Toggle Content
              </Button>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
};

export default MyGridComponent;

const SpecialComponent = (props: any) => {
  return <div>This is special content! {props.content}</div>;
};
