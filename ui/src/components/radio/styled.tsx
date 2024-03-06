import { TooltipProps, Tooltip, tooltipClasses } from '@mui/material';
import styled from 'styled-components';

export const StyledTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} arrow classes={{ popper: className }} placement='right' />
))(() => ({
  [`& .${tooltipClasses.arrow}`]: {
    color: '#000000',
  },
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: '#000000',
  },
}));
