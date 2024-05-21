import { SetStateAction } from 'react';

export interface IDialogBoxProps {
  open: boolean;
  state: (data: SetStateAction<boolean>) => void;
  title: string;
  text: string;
  redirect: () => void;
  datatestid?: string;
}
