import {
  IconButton,
  Link,
  Tooltip,
  TooltipProps,
  styled,
  tooltipClasses,
} from "@mui/material";
import { Fragment } from "react";

const BootstrapTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} arrow classes={{ popper: className }} placement="right" />
))(({ theme }) => ({
  [`& .${tooltipClasses.arrow}`]: {
    color: theme.palette.common.black,
  },
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: "#f5f5f9",
    color: "rgba(0, 0, 0, 0.87)",
    maxWidth: 220,
    fontSize: theme.typography.pxToRem(12),
    border: "1px solid #dadde9",
  },
}));

export default function CustomTooltip(props: any) {
  const handleClick = () => {
    props.behaviour();
  };

  return (
    <BootstrapTooltip
      title={
        <Fragment>
          <p>{props.text}</p>
          <Link component="button" variant="caption" onClick={handleClick}>
            {props.link}
          </Link>
        </Fragment>
      }
    >
      <IconButton>{props.icon}</IconButton>
    </BootstrapTooltip>
  );
}
