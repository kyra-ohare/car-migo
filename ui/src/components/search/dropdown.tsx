import { TextField } from '@mui/material';
import { StyledAutocomplete } from './styled';
import { locations } from '../../constants/location';

export default function LocationDropdown(props: any) {
  return (
    <StyledAutocomplete
      disablePortal
      id={props.id}
      value={props.value.value}
      isOptionEqualToValue={(
        option: { label: string; value: any },
        value: { value: any }
      ) => option?.value === value?.value}
      onChange={(_event, value) =>
        props.onChange(props.name, value?.value || '')
      }
      options={locations}
      sx={{ width: 300, mr: 0.5 }}
      renderInput={(params) => (
        <TextField
          {...params}
          label={props.label}
          name={props.name}
          error={props.formikTouched && Boolean(props.formikErrors)}
          helperText={props.formikTouched && props.formikErrors}
        />
      )}
    />
  );
}
