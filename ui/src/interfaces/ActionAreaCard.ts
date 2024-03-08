import { ReactNode } from 'react';

export interface IActionAreaCardProps {
  id: string;
  label: string;
  icon: ReactNode;
  imageAlt: string;
  title: string;
  text: ReactNode;
  dataTestId: string;
}
