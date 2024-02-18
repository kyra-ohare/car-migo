import React from 'react';
import { Grid, Typography } from '@mui/material';

interface ICard {
  id: string;
  label: string;
  icon: React.ReactNode;
  imageAlt: string;
  title: string;
  text: React.ReactNode;
}

export default function ActionAreaCard(props: ICard) {
  return (
    <Grid container sx={{ marginTop: '20px', marginRight: '50px' }}>
      <Grid
        item
        xs={3}
        sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
      >
        {props.icon}
      </Grid>
      <Grid item xs={9}>
        <Typography gutterBottom variant='h5' component='div'>
          {props.title}
        </Typography>
        <Typography variant='body1' color='text.primary'>
          {props.text}
        </Typography>
      </Grid>
    </Grid>
  );
}
