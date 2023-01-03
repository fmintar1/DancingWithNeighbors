import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { Styles } from 'app/shared/model/enumerations/styles.model';

export interface IFriends {
  id?: number;
  name?: string | null;
  birthdate?: string | null;
  location?: string | null;
  styles?: Styles | null;
  availability?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IFriends> = {};
