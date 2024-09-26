import { Grid2, Typography } from '@mui/material';
import { IActionAreaCardProps } from '../../interfaces/ActionAreaCard';

export default function ActionAreaCard(props: IActionAreaCardProps) {
  return (
    <Grid2
      container
      sx={{ marginTop: '20px', marginRight: '50px' }}
      data-testid={props.datatestid}
    >
      <Grid2
        size={{ xs: 3 }}
        sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
      >
        {props.icon}
      </Grid2>
      <Grid2 size={{ xs: 9 }}>
        <Typography gutterBottom variant='h5' component='div'>
          {props.title}
        </Typography>
        <Typography variant='body1' color='text.primary'>
          {props.text}
        </Typography>
      </Grid2>
    </Grid2>
  );
}
