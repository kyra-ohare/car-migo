import { useState } from 'react';
import { Autocomplete, TextField } from '@mui/material';

export default function ComboBox() {
  const [autocompleteInputValue, setAutocompleteInputValue] =
    useState<string>('');
  const [isAutocompleteOpen, setIsAutocompleteOpen] = useState(false);

  const updateAutocompletePopper = () => {
    setIsAutocompleteOpen(!isAutocompleteOpen);
  };

  return (
    <Autocomplete
      id='autocompleteSearch'
      data-testid='autocomplete-search'
      disableClearable={true}
      renderOption={(option) => option.title}
      getOptionLabel={(option) => option.title}
      renderInput={(params) => (
        <TextField {...params} label='openOnFocus: false' variant='outlined' />
      )}
      options={top100Films} // Assuming you have a list of films
      clearOnEscape={true}
      onInputChange={(_event, value) => {
        setAutocompleteInputValue(value);
      }}
      inputValue={autocompleteInputValue}
      open={isAutocompleteOpen}
      onOpen={updateAutocompletePopper}
      onClose={updateAutocompletePopper}
      style={{ width: 300 }}
      ListboxProps={{ 'data-testid': 'list-box' }}
    />
  );
}

// Top 100 films as rated by IMDb users. [^1^][1]
export const top100Films = [
  { title: 'The Shawshank Redemption', year: 1994 },
  { title: 'Avatar', year: 2010 },
  // ... other film entries ...
];
