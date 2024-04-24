import { Box, Typography } from '@mui/material';

export default function FloatingText() {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '15vh',
        overflow: 'hidden',
        backgroundColor: 'yellow',
      }}
    >
      <Typography
        variant='h4'
        component='h1'
        // sx={{ animation: `${float} 3s ease-in-out infinite` }}
      >
        As a driver, you don't have any journeys. Click here to create some.
      </Typography>
    </Box>
  );
}

// const float = keyframes`
// 0%, 100% {
//   transform: translateY(0);
// }
// 50% {
//   transform: translateY(-20px);
// }
// `;
