import { Box, Typography, keyframes } from '@mui/material';

export default function FloatingText(prop: any) {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '15vh',
        overflow: 'hidden',
      }}
    >
      <Typography
        variant='h4'
        component='h1'
        sx={{ animation: `${float} 4s ease-in-out infinite` }}
      >
        {prop.text}
      </Typography>
    </Box>
  );
}

const float = keyframes`
0%, 100% {
  transform: translateY(0);
}
50% {
  transform: translateY(-20px);
}
`;
