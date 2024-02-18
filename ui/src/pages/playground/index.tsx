import { Autocomplete, TextField } from '@mui/material';

export default function Playground() {
  return (
    <Autocomplete
      disablePortal
      id='combo-box-demo'
      options={top100Films}
      sx={{ width: 300 }}
      renderInput={(params) => <TextField {...params} label='Movie' />}
      onChange={(_event, value) => {}}
    />
  );
}

const top100Films = [
  { label: 'The Shawshank Redemption', year: 1994 },
  { label: 'The Godfather', year: 1972 },
  { label: 'The Godfather: Part II', year: 1974 },
  { label: 'The Dark Knight', year: 2008 },
];
