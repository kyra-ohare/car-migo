import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  IconButton,
  Radio,
  RadioGroup,
} from '@mui/material';
import { Info } from '@mui/icons-material';
import { StyledTooltip } from './styled';
import { IYesNoRadioButtonsGroupProps } from '../../interfaces';

export default function YesNoRadioButtonsGroup(props: IYesNoRadioButtonsGroupProps) {
  return (
    <FormControl>
      <Grid container spacing={1} sx={{ marginTop: '20px', display: 'flex' }}>
        <Grid item xs={8}>
          <FormLabel
            id='demo-controlled-radio-buttons-group'
            sx={{
              color: 'black',
              display: 'flex',
            }}
          >
            {props.label}
          </FormLabel>
        </Grid>
        <Grid>
          <StyledTooltip title={props.tooltip}>
            <IconButton>
              <Info />
            </IconButton>
          </StyledTooltip>
        </Grid>
        <Grid>
          <RadioGroup
            row
            aria-labelledby='demo-controlled-radio-buttons-group'
            name='controlled-radio-buttons-group'
            defaultValue='yes'
            {...props}
          >
            <FormControlLabel label='Yes' value='yes' control={<Radio />} />
            <FormControlLabel label='No' value='no' control={<Radio />} />
          </RadioGroup>
        </Grid>
      </Grid>
    </FormControl>
  );
}
