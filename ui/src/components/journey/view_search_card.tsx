import { IJourneyResponseProps } from '../../interfaces';
import ViewDriverCard from './view_driver_card';

export default function ViewSearchCard(props: IJourneyResponseProps) {
  return <ViewDriverCard journey={props.journey} />;
}
