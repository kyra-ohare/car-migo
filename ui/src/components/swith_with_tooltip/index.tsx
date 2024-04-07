import Switch from '@mui/material/Switch';
import Tooltip from '@mui/material/Tooltip';
import FormControlLabel from '@mui/material/FormControlLabel';
import { ISwitchWithTooltip } from '../../interfaces';

export default function SwitchWithTooltip(props: ISwitchWithTooltip) {
  return (
    <Tooltip title={props.tooltipText}>
      <FormControlLabel
        label={props.label}
        labelPlacement='start'
        control={
          <Switch
            checked={props.isChecked}
            onChange={props.handleSwithWithTooltip}
            inputProps={{ 'aria-label': 'controlled' }}
            data-testid={props.datatestid}
          />
        }
      />
    </Tooltip>
  );
}
