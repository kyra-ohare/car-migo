import React from 'react';
import TextField from '@mui/material/TextField';

function NumberInput() {
  const [number, setNumber] = React.useState('');

  const handleChange = (_event) => {
    setNumber(_event.target.value);
  };

  return (
    <TextField
      label="Number Input"
      type="number"
      value={number}
      onChange={handleChange}
      variant="outlined"
      fullWidth
    />
  );
}

export default NumberInput;
