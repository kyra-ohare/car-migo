import { TooltipProps, Tooltip, tooltipClasses } from '@mui/material';
import styled from 'styled-components';

export const StyledTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} arrow classes={{ popper: className }} placement='right' />
))(() => ({
  [`& .${tooltipClasses.arrow}`]: {
    color: '#000000',
  },
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: '#f5f5f9',
    color: 'rgba(0, 0, 0, 0.87)',
    maxWidth: 220,
    fontSize: 12,
    border: '1px solid #dadde9',
  },
}));
