import { Button, Card, Grid } from '@mui/material';
import styled from 'styled-components';

export const StyledGrid = styled(Grid)`
  justify-content: center;
  align-items: center;
`;
export const StyledJourneyCard = styled(Card)`
  transition: transform 0.2s ease-in-out;
  margin-bottom: 15px;

  &:hover {
    transform: translateY(-5px);
  }
`;

export const StyledButton = styled(Button)`
  color: #ff4d4d;
  &:hover {
    background-color: #f2f2f2;
  }
`;
