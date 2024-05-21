import { ReactNode } from 'react';

export interface ICustomTooltipProps {
  behaviour: () => void;
  text: string;
  link: string;
  icon: ReactNode;
}
