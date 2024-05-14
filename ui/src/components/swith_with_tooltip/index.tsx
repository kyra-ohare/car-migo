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
            inputProps={{
              'aria-label': 'controlled',
              // eslint-disable-next-line @typescript-eslint/ban-ts-comment
              //@ts-ignore
              'data-testid': `${props.datatestid}-switch`,
            }}
            data-testid={props.datatestid}
          />
        }
      />
    </Tooltip>
  );
}
