import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  IconButton,
  Radio,
  RadioGroup,
  Tooltip,
  TooltipProps,
  styled,
  tooltipClasses,
} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

const BootstrapTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} arrow classes={{ popper: className }} placement="right" />
))(({ theme }) => ({
  [`& .${tooltipClasses.arrow}`]: {
    color: theme.palette.common.black,
  },
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: theme.palette.common.black,
  },
}));

export default function YesNoRadioButtonsGroup(props: any) {
  return (
    <FormControl>
      <Grid container spacing={1} sx={{ marginTop: "20px", display: "flex" }}>
        <Grid item xs={8}>
          <FormLabel
            id="demo-controlled-radio-buttons-group"
            sx={{
              color: "black",
              display: "flex",
            }}
          >
            {props.label}
          </FormLabel>
        </Grid>
        <Grid>
          <BootstrapTooltip title={props.tooltip}>
            <IconButton>
              <InfoIcon />
            </IconButton>
          </BootstrapTooltip>
        </Grid>
        <Grid>
          <RadioGroup
            row
            aria-labelledby="demo-controlled-radio-buttons-group"
            name="controlled-radio-buttons-group"
            defaultValue="yes"
            {...props}
          >
            <FormControlLabel label="Yes" value="yes" control={<Radio />} />
            <FormControlLabel label="No" value="no" control={<Radio />} />
          </RadioGroup>
        </Grid>
      </Grid>
    </FormControl>
  );
}

// Example of how this is called
              {/* <Grid item xs={12}>
                <YesNoRadioButtonsGroup
                  label="Do you want to be a passenger?"
                  tooltip="As a passenger, you can book journeys."
                  value={isPassenger}
                  onChange={(event: {
                    target: { value: React.SetStateAction<string> };
                  }) => {
                    setIsPassenger(event.target.value);
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <YesNoRadioButtonsGroup
                  label="Do you want to be a driver?"
                  tooltip="As a driver, you can create journeys."
                  value={isDriver}
                  onChange={(event: {
                    target: { value: React.SetStateAction<string> };
                  }) => {
                    setIsDriver(event.target.value);
                  }}
                />
              </Grid> */}