import { Card } from '@mui/material';
import styled from 'styled-components';

export const StyledCard = styled(Card)`
  box-shadow: 05px 6px 5px rgba(0, 0, 0, 0.2);
  padding: 5px;
  transition: transform 0.2s ease-in-out;
  margin-bottom: 15px;
  width: 50%;

  &:hover {
    transform: translateY(-5px);
  }
`;
